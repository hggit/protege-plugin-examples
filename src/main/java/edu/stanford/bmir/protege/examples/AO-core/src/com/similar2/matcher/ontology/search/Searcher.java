package com.similar2.matcher.ontology.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.similar2.matcher.ontology.model.IAnnotations;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.ILocalizedString;
import com.similar2.matcher.ontology.model.INamedEntity;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.LoggerManager;
import com.similar2.matcher.ontology.model.impl.LocalizedString;

/***
 * Singleton class to handle searches of the ontology.
 * 
 * @author Chris Ahern
 * 
 */
public class Searcher {

	private HashMap<ILocalizedString, List<SearchData>> searchMap = null;

	private static Searcher instance = null;

	// Make the constructor private to enforce the Singleton pattern.
	private Searcher() {
		clearSearchMap();
	}

	/***
	 * Returns a Searcher instance (enforcing the Singleton pattern).
	 * 
	 * @return
	 */
	public static Searcher getInstance() {
		if (instance == null) {
			instance = new Searcher();
		}

		return instance;
	}

	/***
	 * Clears the search map - to be used when a new ontology is loaded.
	 */
	public void clearSearchMap() {
		searchMap = new HashMap<ILocalizedString, List<SearchData>>();
	}
	
	/***
	 * Adds a search string to the search store with its associated entity
	 * and searchDataType.
	 * 
	 * @param searchString
	 * @param entity
	 */
	public void add(ILocalizedString searchString, 
					INamedEntity entity) {
		if (entity instanceof IPrimaryEntityClass) {
			add(searchString, entity, SearchDataType.TAXONOMY);
		} else if (entity instanceof IPrimaryEnumeratedClass) {
			add(searchString, entity, SearchDataType.VALUE);
		}  else if (entity instanceof IEnumeratedProperty) {
			add(searchString, entity, SearchDataType.PROPERTY);
		} else {
			//throw new Exception("Unknown entity type");
			LoggerManager.LOGGER.error("\nUnknown entity type {}", entity.getClass());
		}
	}	
	/***
	 * Adds a search string to the search store with its associated entity
	 * and searchDataType.
	 * 
	 * @param searchString
	 * @param entity
	 */
	public void add(ILocalizedString searchString, 
					INamedEntity entity,
					SearchDataType searchDataType) {
		ArrayList<SearchData> searchDataList = null;
		
		LocalizedString lowerCaseSearchString = 
			new LocalizedString(searchString.getLanguage(), 
								searchString.getString().toLowerCase());
		
		if (searchMap.containsKey(lowerCaseSearchString)) {
			searchDataList = (ArrayList<SearchData>) searchMap.get(lowerCaseSearchString);
			searchDataList.add(new SearchData(entity, searchDataType));
		} else {
			searchDataList = new ArrayList<SearchData>();
			searchDataList.add(new SearchData(entity, searchDataType));
			searchMap.put(lowerCaseSearchString, searchDataList);
		}
	}

	public void add(IAnnotations searchAnnotations, 
			INamedEntity entity,
			SearchDataType searchDataType) {
		for(ILocalizedString annotation: searchAnnotations) {
			add(annotation, entity, searchDataType);
		}
	}

	/**
	 * Removes a search string and the associated entity from the search store
	 * 
	 * @param searchString
	 *            the search string to remove.
	 * @param entity
	 *            the named entity associated to the search string
	 */
	public void remove(ILocalizedString searchString, INamedEntity entity) {
		LocalizedString lowerCaseSearchString = new LocalizedString(
				searchString.getLanguage(), searchString.getString()
						.toLowerCase());
		if (searchMap.containsKey(lowerCaseSearchString)) {
			List<SearchData> searchDataList = (List<SearchData>) searchMap.get(lowerCaseSearchString);
			SearchData sd = null;
			if (entity instanceof IPrimaryEntityClass) {
				sd = new SearchData(entity, SearchDataType.TAXONOMY);
			} else if (entity instanceof IPrimaryEnumeratedClass) {
				sd = new SearchData(entity, SearchDataType.VALUE);
			}  else if (entity instanceof IEnumeratedProperty) {
				sd = new SearchData(entity, SearchDataType.PROPERTY);
			} else {
				throw new IllegalArgumentException("unknown named entity type");
			}

			searchDataList.remove(sd);
			if (searchDataList.size() == 0) {
				searchMap.remove(lowerCaseSearchString);
			}
		}
	}

	/***
	 * Returns a list of entities matching the searchString.
	 * 
	 * @param searchString
	 * @param filterOnLanguage
	 * @return
	 */
	public List<INamedEntity> findAll(ILocalizedString searchString, 
			boolean searchOptionTaxonomy,
			boolean searchOptionDefinitions,
			boolean searchOptionProperties,
			boolean searchOptionValues,
			boolean filterOnLanguage) {

		String lowerCaseSearchString = searchString.getString().toLowerCase();
		String language = searchString.getLanguage();

		List<INamedEntity> results = new ArrayList<INamedEntity>();
		for (ILocalizedString candidateString : searchMap.keySet()) {
			// Filter by language first.
			if (filterOnLanguage
					&& !language.equals(candidateString.getLanguage())
					&& !candidateString.getLanguage().equals("default")) {
				continue;
			}

			if (candidateString.getString().contains(lowerCaseSearchString)) {
				for (SearchData foundSearchData : searchMap.get(candidateString)) {
					if (!results.contains(foundSearchData)) {
						if (searchOptionTaxonomy && 
							foundSearchData.getSearchDataType() == SearchDataType.TAXONOMY)
							results.add(foundSearchData.getEntity());
						else if (searchOptionDefinitions && 
							foundSearchData.getSearchDataType() == SearchDataType.DESCRIPTION)
							results.add(foundSearchData.getEntity());
						else if (searchOptionProperties && 
								foundSearchData.getSearchDataType() == SearchDataType.PROPERTY)
							results.add(foundSearchData.getEntity());
						else if (searchOptionValues && 
								foundSearchData.getSearchDataType() == SearchDataType.VALUE)
							results.add(foundSearchData.getEntity());
					}
				}
			}
		}

		return results;
	}
}
