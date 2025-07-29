package cn.tech.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cn.tech.model.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		
		try (PrintWriter out=resp.getWriter()){
			ArrayList<Cart> cartlist =new ArrayList<>();
			
			
			int id=Integer.parseInt(req.getParameter("id"));
			Cart cm=new Cart();
			cm.setId(id);
			cm.setQuantity(1);
			
			
			HttpSession session=req.getSession();
			ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
			
			if (cart_list==null) {
			cartlist.add(cm);
			session.setAttribute("cart-list", cartlist);
			resp.sendRedirect("index.jsp");
			} else {
				cartlist=cart_list;
				boolean exists=false;
				
				
				
				for (Cart c : cartlist) {
					if (c.getId()==id) {
					exists=true;
					out.print("<h3 style='color:crimson; text-align:canter'>Item Already exists in Cart.<a href='cart.jsp'>Go to Cart Page</a></h3>");
					}
					
				}	
				if (!exists) {
					cartlist.add(cm);
					resp.sendRedirect("index.jsp");
				}
			}
		} 
	}
}
