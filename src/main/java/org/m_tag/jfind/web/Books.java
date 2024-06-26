/*******************************************************************************
 * (c) Copyright IBM Corporation 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.m_tag.jfind.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.m_tag.jfind.books.Book;
import org.m_tag.jfind.books.Config;
import org.m_tag.jfind.books.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//  curl http://192.168.200.190:9080/FindBooks/books --get --data-urlencode author=
@Path("/books")
public class Books {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Context
	private ServletContext context;

	private Config config;

	@GET
	@Produces("application/json")
	public Response get(@QueryParam("author") final String author, @QueryParam("title") final String title,
			@QueryParam("keyword") final String keyword) throws IOException, ClassNotFoundException, SQLException {

		this.config = getConfig();

		final Query query = new Query();
		query.setAuthor(author);
		query.setTitle(title);
		query.setKeyword(keyword);
		query.setExists(false);
		logger.info("finding: {}", query);
		List<Book> books = config.find(query).toList();
		logger.info("found: size:{}", books.size());
		return Response.status(Response.Status.OK).entity(books).build();
	}

	private Config getConfig() throws IOException {
		if (config == null) {
			String file = System.getenv().get(Config.JFINDBOOKS_JSON);
			if (file == null || file.length() == 0) {
				file = context.getRealPath("/WEB-INF/jFindBooks.json");
			}
			logger.info("loaing config : {}", file);
			config = Config.getConfig(file);
			logger.info("loaded config : {} : {}", file, config);
		}
		return config;
	}
}