package org.wjihle.swifts.entity;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "swiftendpoint", namespace = @ApiNamespace(ownerDomain = "wjihle.org", ownerName = "wjihle.org", packagePath = "swifts.entity"))
public class SwiftEndpoint {

	
	/*
	 * Author: Bill Ihle
	 */
	
	
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listSwift")
	public CollectionResponse<Swift> listSwift(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Swift> execute = null;

		try {
			System.out.println("list all swifts...");
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Swift.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Swift>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Swift obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Swift> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getSwift")
	public Swift getSwift(Swift swift, @Named("id") Long id) {
		System.out.println("getSwift(@Named(\"id\"):" + id);
		PersistenceManager mgr = getPersistenceManager();
		try {
			swift = mgr.getObjectById(Swift.class, id);
			System.out.println("Found swift with id " + swift.getId());
		} catch (JDOObjectNotFoundException jdoe) {
			System.out.println("**ERROR**");
			System.out.println("Swift with id " + id + " not found");
		} finally {
			mgr.close();
		}
		return swift;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param swift
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertSwift", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
			Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
			com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
	public Swift insertSwift(Swift swift, User user) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (user == null) 
				throw new UnauthorizedException("User is Not Valid");
			if (containsSwift(swift)) 
				throw new EntityExistsException("Object already exists");
			mgr.makePersistent(swift);
		} catch (UnauthorizedException ue) {
			System.out.println("ERROR:" + ue.getMessage());
		} catch (EntityExistsException e) {
			System.out.println("ERROR:" + e.getMessage());
		} finally {
			mgr.close();
		}
		return swift;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param swift
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateSwift")
	public Swift updateSwift(Swift swift) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSwift(swift)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(swift);
		} finally {
			mgr.close();
		}
		return swift;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeSwift")
	public void removeSwift(Swift swift, @Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			swift = mgr.getObjectById(Swift.class, id);
			mgr.deletePersistent(swift);
			System.out.println("Swift with id "+id+" deleted.");
		} catch (JDOObjectNotFoundException jdoe) {
			System.out.println("**ERROR**");
			System.out.println("Swift with id " + id + " not found");
		} finally {
			mgr.close();
		}
	}

	private boolean containsSwift(Swift swift) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Swift.class, swift.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
