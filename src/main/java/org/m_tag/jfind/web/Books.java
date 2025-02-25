package org.m_tag.jfind.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.m_tag.jfind.books.Book;
import org.m_tag.jfind.books.Config;
import org.m_tag.jfind.books.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource of api To find books.
 * <p/>
 * 
 * <pre>
 * curl http://192.168.200.190:9080/FindBooks/books --get --data-urlencode author=${AUTHOR}
 * </pre>
 */
@Path("/books")
public class Books {
  /**
   * logger.
   */
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * books finder.
   */
  private final Config finders;

  /**
   * constructor.
   *
   * @param app application class.
   */
  public Books(final FindApplication app) {
    super();
    this.finders = app.getFinders();
  }

  /**
   * find books with finders.
   *
   * @param genre genre of book(set null if not specified).
   * @param author author of book(set null if not specified).
   * @param title title of book(set null if not specified).
   * @param keyword keyword(any of fields) of book(set null if not specified).
   * @return list of books(returns empty if found nothing).
   * @throws IOException error in finding books
   * @throws SQLException error in JDBC query.
   */
  @GET
  @Produces("application/json")
  public Response get(
      @QueryParam("genre") final String genre,
      @QueryParam("author") final String author,
      @QueryParam("title") final String title, 
      @QueryParam("keyword") final String keyword,
      @DefaultValue("100") @QueryParam("maxCount") final int maxCount)
      throws IOException, SQLException {

    final Query query = new Query();
    query.setAuthor(author);
    query.setGenre(genre);
    query.setTitle(title);
    query.setKeyword(keyword);
    query.setExists(false);
    query.setMaxCount(maxCount);
    Stream<Book> books = finders.find(query).sorted();
    Object values;
    if (maxCount <= 0) {
      values = books;
    } else {
      final List<Book> list = books.toList();
      logger.info("query: {}, found: size:{}, maxCount:{}", query, list.size(), maxCount);
      values = list;
    }
    return Response.status(Response.Status.OK).entity(values).build();
  }
}
