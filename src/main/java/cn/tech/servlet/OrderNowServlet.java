package cn.tech.servlet;

import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import cn.tech.connection.DbCon;
import cn.tech.dao.OrderDao;
import cn.tech.model.Cart;
import cn.tech.model.Order;
import cn.tech.model.Product;
import cn.tech.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/order-now")
public class OrderNowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (PrintWriter out = resp.getWriter()) {

			User auth = (User) req.getSession().getAttribute("auth");
			if (auth != null) {

				String productId = req.getParameter("id");
				out.print(productId);
				int productQuantity = Integer.parseInt(req.getParameter("quantity"));
				LocalDate today = LocalDate.now();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				String dateOnly = formatter.format(today);

//				String date=req.getParameter("date");

				if (productQuantity <= 0) {
					productQuantity = 1;
				}

				Order orderModel = new Order();
				orderModel.setId(Integer.parseInt(productId));
				orderModel.setUid(auth.getId());
				orderModel.setQuantity(productQuantity);
				orderModel.setDate(formatter.format(today));

				OrderDao orderDao = new OrderDao(DbCon.getConnection());
				boolean result = orderDao.insertOrder(orderModel);
				out.print(result);

				if (result) {
					ArrayList<Cart> cart_list = (ArrayList<Cart>) req.getSession().getAttribute("cart-list");
					if (cart_list != null) {
						for (Cart c : cart_list) {
							if (c.getId() == Integer.parseInt(productId)) {
								cart_list.remove(cart_list.indexOf(c));
								break;
							}
						}
					}

					resp.sendRedirect("orders.jsp");
				} else {
					out.print("order failed");
				}
			} else {
				out.print("this page is not redirecting");
				// resp.sendRedirect("login.jsp");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
