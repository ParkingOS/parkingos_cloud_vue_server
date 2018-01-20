package parkingos.com.bolink.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ShopMemberManageService {
    String quickquery(HttpServletRequest req, HttpServletResponse resp);

    String create(HttpServletRequest req, HttpServletResponse resp);

    String delete(HttpServletRequest req, HttpServletResponse resp);

    String editpass(HttpServletRequest req, HttpServletResponse resp);
}
