package parkingos.com.bolink.service;

import javax.servlet.http.HttpServletRequest;

public interface ShopMemberManageService {
    String quickquery(HttpServletRequest req);

    String create(HttpServletRequest req);

    String delete(HttpServletRequest req);

    String editpass(HttpServletRequest req);

    String getRoleByConditions(Long roleId, Long shopId);

    String getShopUsers(Long shopId);
}
