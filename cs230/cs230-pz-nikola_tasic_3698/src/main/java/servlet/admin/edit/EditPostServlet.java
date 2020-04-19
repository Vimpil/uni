package servlet.admin.edit;

import com.sun.corba.se.spi.oa.ObjectAdapter;
import config.Config;
import database.dao.BlogPostDAO;
import database.entity.BlogPost;
import sun.applet.AppletResourceLoader;
import util.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/admin/edit/*")
public class EditPostServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idBlogPost = UrlUtil.getUrlBase(request.getRequestURL().toString());
		request.setAttribute("idBlogPost", idBlogPost);
		request.getRequestDispatcher("/admin/edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idBlogPostString = request.getParameter("idBlogPost");
		long idBlogPost;
		String title = request.getParameter("title");
		String slug = request.getParameter("slug");
		String preview = request.getParameter("preview");
		String body = request.getParameter("body");
		BlogPostDAO blogPostDAO = new BlogPostDAO();
		try {
			idBlogPost = Long.parseLong(idBlogPostString);
			BlogPost blogPost = blogPostDAO.find(idBlogPost);
			blogPost.setTitle(title);
			blogPost.setSlug(slug);
			blogPost.setBody(body);
			blogPost.setPreview(preview);
			blogPostDAO.update(blogPost);
		} catch (NumberFormatException ex) {
			BlogPost blogPost = new BlogPost();
			blogPost.setTitle(title);
			blogPost.setSlug(slug);
			blogPost.setBody(body);
			blogPost.setPreview(preview);
			blogPost.setDatePosted(LocalDate.now());
			blogPost.setAuthor((String) Config.getProperties().get("author"));
			blogPostDAO.create(blogPost);
		}
		response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
	}
}
