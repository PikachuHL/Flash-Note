package cn.hellopika.flashnote.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hellopika.flashnote.model.dto.request.CreateMemoDto;
import cn.hellopika.flashnote.model.dto.request.MemoEditDto;
import cn.hellopika.flashnote.model.dto.response.CreateMemoRespDto;
import cn.hellopika.flashnote.model.entity.Memo;
import cn.hellopika.flashnote.model.entity.Tag;
import cn.hellopika.flashnote.service.MemoService;
import cn.hellopika.flashnote.util.QiniuUtils;
import cn.hellopika.flashnote.util.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记的 controller
 **/

@RestController
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @Autowired
    private QiniuUtils qiniuUtils;

    /**
     * 创建笔记
     * @param dto
     * @return
     */
    @PostMapping("/create")
    public ApiResult createMemo(@RequestBody CreateMemoDto dto){
        String userId = StpUtil.getLoginIdAsString();
        // 创建完成后会返回笔记的一些信息，把这些信息返回给前端
        CreateMemoRespDto memoInfo = memoService.createMemo(dto, userId);
        return ApiResult.success(memoInfo);
    }

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/getTags")
    public ApiResult getTags(){
        String userId = StpUtil.getLoginIdAsString();
        List<String> tagNames = memoService.getTags(userId);
        return ApiResult.success(tagNames);
    }

    /**
     * 查询所有笔记
     * 可根据标签查询
     * @return
     */
    @GetMapping("/list")
    public ApiResult getMemos(String tagName){
        String userId = StpUtil.getLoginIdAsString();
        List<Memo> memos = memoService.getMemos(userId, tagName);

        return ApiResult.success(memos);
    }

    /**
     * 删除笔记
     * @param memoId
     * @return
     */
    @PostMapping("/del")
    public ApiResult delMemo(String memoId){
        String userId = StpUtil.getLoginIdAsString();
        memoService.delMemo(userId, memoId);

        return ApiResult.success();
    }


    /**
     * 编辑笔记
     * @param dto
     * @return
     */
    @PostMapping("/edit")
    public ApiResult editMemo(@RequestBody MemoEditDto dto){
        String userId = StpUtil.getLoginIdAsString();
        dto.setUserId(userId);
        memoService.editMemo(dto);
        return ApiResult.success();
    }

    /**
     * 获取上传图片需要的授权信息
     * @return
     */
    @PostMapping("/img/getToken")
    public ApiResult getImgUpToken(){
        String upToken = qiniuUtils.getImgUpToken();

        return ApiResult.success(upToken);
    }


}
