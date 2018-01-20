package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.service.ProductPackageService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductPackageAction {

    Logger logger = Logger.getLogger(ProductPackageAction.class);

    @Autowired
    private ProductPackageService productPackageService;


    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = productPackageService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp){

        //创建时间
        Long createTime = System.currentTimeMillis()/1000;
        //车场编号
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String period = StringUtils.decodeUTF8(RequestUtil.getString(request, "period"));
        String carTypeId = RequestUtil.getString(request,"car_type_id");
        Double price = RequestUtil.getDouble(request, "price", 0.0);
        //月卡名称
        String p_name = StringUtils.decodeUTF8(RequestUtil.processParams(request, "p_name"));
        //月卡描述
        String describe =StringUtils.decodeUTF8(RequestUtil.getString(request, "describe"));
        //生成唯一主键
        Long id = productPackageService.getId();

        BigDecimal bigDecimal = new BigDecimal(price+"");

        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setComid(comid);
        productPackageTb.setId(id);
        productPackageTb.setIsDelete(0L);
        productPackageTb.setPeriod(period);
        productPackageTb.setCarTypeId(carTypeId);
        productPackageTb.setPrice(bigDecimal);
        productPackageTb.setpName(p_name);
        productPackageTb.setDescribe(describe);
        productPackageTb.setCreateTime(createTime);
        JSONObject result = productPackageService.createProduct(productPackageTb);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp){

        Long id =RequestUtil.getLong(request, "id", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        JSONObject result = productPackageService.deleteProduct(id,comid);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = productPackageService.editProduct(reqParameterMap);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
