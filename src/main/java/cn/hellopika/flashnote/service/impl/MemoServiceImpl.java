package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.mapper.MemoMapper;
import cn.hellopika.flashnote.mapper.MemoTagMapper;
import cn.hellopika.flashnote.mapper.TagMapper;
import cn.hellopika.flashnote.model.dto.request.CreateMemoDto;
import cn.hellopika.flashnote.model.dto.request.MemoEditDto;
import cn.hellopika.flashnote.model.dto.response.CreateMemoRespDto;
import cn.hellopika.flashnote.model.entity.Memo;
import cn.hellopika.flashnote.model.entity.MemoTag;
import cn.hellopika.flashnote.model.entity.Tag;
import cn.hellopika.flashnote.service.MemoService;
import cn.hellopika.flashnote.util.DateTimeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记功能的 业务层实现类
 **/
@Service
@Slf4j
public class MemoServiceImpl implements MemoService {

    @Autowired
    private MemoMapper memoMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MemoTagMapper memoTagMapper;

    /**
     * 创建笔记
     *
     * @param dto
     * @param userId
     */
    @Override
    @Transactional
    public CreateMemoRespDto createMemo(CreateMemoDto dto, String userId) {

        // 创建笔记
        Memo memo = new Memo();
        memo.setContent(dto.getContent());
        memo.setDevice(dto.getDevice());
        memo.setCreateTime(DateTimeUtils.getNowString());
        memo.setUserId(userId);
        if (!(dto.getParentId() == null)) {
            memo.setParentId(dto.getParentId());
        }
        memoMapper.insert(memo);

        // 保存笔记对应的标签
        List<String> tagNames = saveMemoTags(memo);

        return new CreateMemoRespDto(memo.getId(), memo.getContent(), memo.getCreateTime(), memo.getDevice(), memo.getParentId(), tagNames);


    }

    /**
     * 保存笔记内容中的标签
     * @param memo
     * @return
     */
    public List<String> saveMemoTags(Memo memo) {
        // 使用jsoup解析笔记内容字符串（笔记内容为html格式）
        String text = Jsoup.parse(memo.getContent()).body().text();
        // 使用正则表达式，提取出内容中的标签名
        List<String> tagNames = extractTagNames(memo.getContent());

        for (String tagName : tagNames) {
            // 判断标签是否存在
            Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("tag_name", tagName));
            if (tag == null) {
                // 保存 tag
                tag = new Tag();
                tag.setTagName(tagName);
                tag.setUserId(memo.getUserId());
                tagMapper.insert(tag);
            }


            // 将memo和tag的对应关系保存到中间表
            MemoTag memoTag = new MemoTag();
            memoTag.setMemoId(memo.getId());
            memoTag.setTagId(tag.getId());
            memoTagMapper.insert(memoTag);
        }


        return tagNames;
    }

    /**
     * 从笔记内容中取出标签
     * @param memoContent
     * @return
     */
    public List<String> extractTagNames(String memoContent) {
        // 使用正则表达式提取 #xxx 格式的标签
        Matcher matcher = Pattern.compile("#(\\w|[^\\x00-\\xff]|/|\\\\)+(\\s)?", Pattern.DOTALL).matcher(memoContent);
        List<String> tagNames = new ArrayList<>();
        while (matcher.find()) {
            tagNames.add(matcher.group());
        }
        return tagNames;
    }


    /**
     * 获取标签列表
     * @param userId
     * @return
     */
    @Override
    public List<String> getTags(String userId) {
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>().eq("user_id", userId));
        List<String> tagNames = new ArrayList<>();
        for(Iterator<Tag> iter = tags.iterator(); iter.hasNext();){
            // next() 方法返回当前元素，然后将 cursor 后移一位
            String tagName = iter.next().getTagName();
            tagNames.add(tagName);
        }
        return tagNames;
    }

    /**
     * 查询笔记列表
     * 可根据标签查询笔记
     * @param userId
     * @param tagName
     * @return
     */
    @Override
    public List<Memo> getMemos(String userId, String tagName) {
        List<Memo> memos = new ArrayList<>();
        if (StringUtils.isNotEmpty(tagName)){
            memos = memoMapper.getMemosByTag(userId, "#" + tagName);
        }else {
            // 如果不选标签，就是查询所有
            memos = memoMapper.selectList(new QueryWrapper<Memo>().eq("user_id", userId));
        }

        return memos;
    }

    /**
     * 删除笔记
     * @param userId
     * @param memoId
     */
    @Override
    @Transactional
    public void delMemo(String userId, String memoId) {
        Memo memo = memoMapper.selectById(memoId);
        if(memo == null){
            throw new ServiceException("笔记不存在");
        }

        // 保证每个用户只能删除自己的笔记
        if(!StringUtils.equals(userId, memo.getUserId())){
            throw new ServiceException("没有权限删除笔记");
        }
        // 删除笔记
        memoMapper.deleteById(memoId);

        // 删除笔记对应的标签
        delMemoTags(memoId);
    }

    /**
     * 删除笔记与标签的关联
     * 以及 根据判断看是否删除响应标签
     * @param memoId
     */
    public void delMemoTags(String memoId){
        List<MemoTag> memoTagList = memoTagMapper.selectList(new QueryWrapper<MemoTag>().eq("memo_id", memoId));
        for (MemoTag memoTag:memoTagList) {
            List<MemoTag> checkTag = memoTagMapper.selectList(new QueryWrapper<MemoTag>().eq("tag_id", memoTag.getTagId()).ne("memo_id", memoId));
            if(checkTag.isEmpty()){
                // 如果标签没有对应其他的笔记，就删除该标签
                tagMapper.deleteById(memoTag.getTagId());
            }
            memoTagMapper.deleteById(memoTag.getId());
        }
    }

    /**
     * 编辑笔记
     * TODO 确定edit返回的数据
     * @param dto
     */
    @Override
    @Transactional
    public void editMemo(MemoEditDto dto) {
        Memo memo = memoMapper.selectById(dto.getMemoId());
        if(memo == null){
            throw new ServiceException("笔记不存在");
        }
        if(!StringUtils.equals(memo.getUserId(), dto.getUserId())){
            throw new ServiceException("没有权限修改笔记");
        }

        memo.setContent(dto.getContent());
        memo.setDevice(dto.getDevice());
        memoMapper.updateById(memo);

        // 删除该笔记的所有标签
        delMemoTags(memo.getId());

        // 重建笔记的所有标签
        saveMemoTags(memo);
    }
}
