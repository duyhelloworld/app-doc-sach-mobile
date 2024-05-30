package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import huce.edu.vn.appdocsach.AppContext;
import huce.edu.vn.appdocsach.constants.SearchHistoryConstant;

public class SearchHistoryManager {
    private static SearchHistoryManager manager;
    private final SharedPreferences preferences = AppContext.getContext().getSharedPreferences(SearchHistoryConstant.shareName, Context.MODE_PRIVATE);

    private SearchHistoryManager() {
    }

    public static SearchHistoryManager getInstance() {
        if (manager == null) {
            manager = new SearchHistoryManager();
        }
        return manager;
    }

    public void addSearchTerm(String term) {
        Set<String> historySet = new HashSet<>(getHistorySet());
        historySet.add(term);

        if (historySet.size() > SearchHistoryConstant.maxHistorySize) {
            Iterator<String> iterator = historySet.iterator();
            iterator.next();
            iterator.remove();
        }

        preferences.edit()
                .putStringSet(SearchHistoryConstant.keyName, historySet)
                .apply();
    }

    private Set<String> getHistorySet() {
        return preferences.getStringSet(SearchHistoryConstant.keyName, new HashSet<>());
    }

    public List<String> getHistory() {
        return new ArrayList<>(getHistorySet());
    }

    public void clearHistory() {
        preferences.edit()
                .remove(SearchHistoryConstant.keyName)
                .apply();
    }

    public void remove(String query) {
        Set<String> history = getHistorySet();
        history.remove(query);
        preferences.edit()
                .remove(SearchHistoryConstant.keyName)
                .putStringSet(SearchHistoryConstant.keyName, history)
                .apply();
    }

}
