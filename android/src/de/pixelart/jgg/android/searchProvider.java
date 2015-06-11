package de.pixelart.jgg.android;

import android.content.SearchRecentSuggestionsProvider; 

public class SearchProvider extends SearchRecentSuggestionsProvider { 

   public static final String AUTHORITY = SearchProvider.class.getName(); 

   public static final int MODE = DATABASE_MODE_QUERIES; 

   public SearchProvider() { 
      setupSuggestions(AUTHORITY, MODE); 
   } 
}

