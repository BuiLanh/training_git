/**
 * Copyright(C) 2019  Luvina Sofware
 * AddUserConfirmController.java Aug 1, 2019 Bui Thi Lanh
 */
package manageuser.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manageuser.entities.UserInfor;
import manageuser.logics.TblUserLogic;
import manageuser.logics.impl.TblUserLogicImpl;
import manageuser.utils.Constant;

/**
 * Controller xử lý các logic của màn hình ADM004 Trường hợp Add user
 * 
 * @author Bui Thi Lanh
 *
 */
public class AddUserConfirmController extends HttpServlet {
	// đảm bảo servlet khi đưa vào bộ nhớ và khi lấy ra là cùng 1 version
	private static final long serialVersionUID = 1L;

	/**
	 * Thực hiện get data cần hiển thị cho màn hình ADM004
	 * 
	 * @param HttpServletRequest req: đối tượng HttpServletRequest
	 * @param HttpServletResponse resp: đối tượng HttpServletResponse
	 * @throws ServletException: ngoại lệ chung mà một servlet có thể ném khi lỗi
	 * @throws IOException: lỗi không đọc được file
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// bắt try catch
		try {
			// khởi tạo session
			HttpSession session = req.getSession();
			//Lấy biến đánh dấu từ session
			String passADM003 = (String) session.getAttribute(Constant.PASS_ADM003);
			//nếu passADM003 khác null
			if (passADM003 != null) {
				//xóa session passADM003
				session.removeAttribute(passADM003);
				String key = req.getParameter(Constant.KEY);
				// lấy userInfor từ session về
				UserInfor userInfor = (UserInfor) session.getAttribute(key);
				// nếu user lấy về khác null
				if (userInfor != null) {
					// set key lên req
					req.setAttribute(Constant.KEY, key);
					// set userInfor lên request
					req.setAttribute(Constant.USER_INFOR, userInfor);
					// forward sang màn hình ADM004
					req.getRequestDispatcher(Constant.ADM004).forward(req, resp);
				} else { 
					resp.sendRedirect(req.getContextPath() + Constant.ERROR + "?type=systemError");
				}
			} else {
				resp.sendRedirect(req.getContextPath() + Constant.ERROR + "?type=systemError");
			}
			// show log
		} catch (Exception e) {
			System.out.println("AddUserConfirmController: doGet() " + e.getMessage());
			resp.sendRedirect(req.getContextPath() + Constant.ERROR + "?type=systemError");
		}
	}

	/**
	 * Xử lý khi click vào button OK của ADM004
	 * 
	 * @param HttpServletRequest req: đối tượng HttpServletRequest
	 * @param HttpServletResponse resp: đối tượng HttpServletResponse
	 * @throws ServletException: ngoại lệ chung mà một servlet có thể ném khi lỗi
	 * @throws IOException: lỗi không đọc được file
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// bắt try catch
		try {
			// Khởi tạo session
			HttpSession session = req.getSession();
			// Lấy key
			String key = req.getParameter(Constant.KEY);
			// lấy userInfor từ session về
			UserInfor userInfor = (UserInfor) session.getAttribute(key);
			// remove key
			session.removeAttribute(key);

			// Khởi tạo TblUserLogic, MstJapanLogic
			TblUserLogic tblUserLogic = new TblUserLogicImpl();
			// Khởi tạo check = false
			boolean check = false;
			// Khởi tạo và gán loginName = giá trị loginName của userInfor
			String loginName = userInfor.getLoginName();
			// Khởi tạo và gán email = giá trị email của userInfor
			String email = userInfor.getEmail();
			int userID = userInfor.getUserID();
			// Nếu chưa tồn tại loginName và email
			if (!tblUserLogic.checkExistedLoginName(userID, loginName)
					&& !tblUserLogic.checkExistedEmail(userID, email)) {
				// gọi hàm createUser
				check = tblUserLogic.createUser(userInfor);
				// nếu check = true
				if (check) {
					resp.sendRedirect(req.getContextPath() + Constant.SUCCESS + "?type=add");
				} else {// ngược lại
					resp.sendRedirect(req.getContextPath() + Constant.ERROR + "?type=systemError");
				}
			}
		} catch (Exception e) {
			System.out.println("AddUserConfirmController: doPost() " + e.getMessage());
			resp.sendRedirect(req.getContextPath() + Constant.ERROR + "?type=systemError");
		}
	}
}
