package cn.lili.controller.other;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.PageUtil;
import cn.lili.common.utils.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.system.entity.dos.SensitiveWords;
import cn.lili.modules.system.service.SensitiveWordsService;
import cn.lili.modules.system.utils.SensitiveWordsFilter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理端,敏感词管理接口
 *
 * @author Bulbasaur
 * @date 2020-05-06 15:18:56
 */
@RestController
@Api(tags = "管理端,敏感词管理接口")
@RequestMapping("/manager/sensitiveWords")
public class SensitiveWordsManagerController {

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "敏感词ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<SensitiveWords> get(@PathVariable String id) {
        return ResultUtil.data(sensitiveWordsService.getById(id));
    }

    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<SensitiveWords>> getByPage(PageVO page) {
        return ResultUtil.data(sensitiveWordsService.page(PageUtil.initPage(page)));
    }

    @ApiOperation(value = "添加敏感词")
    @PostMapping
    public ResultMessage<SensitiveWords> add(@Valid SensitiveWords sensitiveWords) {
        if (sensitiveWordsService.save(sensitiveWords)) {
            SensitiveWordsFilter.put(sensitiveWords.getSensitiveWord());
            return ResultUtil.data(sensitiveWords);
        }
        throw new ServiceException(ResultCode.ERROR);
    }

    @ApiOperation(value = "修改敏感词")
    @ApiImplicitParam(name = "id", value = "敏感词ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/{id}")
    public ResultMessage<SensitiveWords> edit(@PathVariable String id, SensitiveWords sensitiveWords) {
        sensitiveWords.setId(id);
        if (sensitiveWordsService.updateById(sensitiveWords)) {
            SensitiveWordsFilter.put(sensitiveWords.getSensitiveWord());
            return ResultUtil.data(sensitiveWords);
        }
        throw new ServiceException(ResultCode.ERROR);
    }

    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "ids", value = "敏感词ID", required = true, dataType = "String", allowMultiple = true, paramType = "path")
    @DeleteMapping(value = "/delByIds/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        for (String id : ids) {
            String name = sensitiveWordsService.getById(id).getSensitiveWord();
            SensitiveWordsFilter.remove(name);
            sensitiveWordsService.removeById(id);
        }
        return ResultUtil.success(ResultCode.SUCCESS);

    }
}
