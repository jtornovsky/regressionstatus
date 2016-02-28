package com.regressionstatus.data.current.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.regressionstatus.data.current.CurrentStatusTableField;

/**
 * 
 * @author jtornovsky
 *
 */
abstract public class AbstractUrlCommandExecuterCommonMethodsHolder {
	
	/**
	 * 
	 * @param fieldValuesList
	 * @param separator
	 * @return
	 */
	protected static String addValueToString(List<String> fieldValuesList, String separator) {
		String valuesAccumulator = "";
		for (String sValue : fieldValuesList) {
			valuesAccumulator += sValue+separator;
		}
		return valuesAccumulator.replaceAll(separator+"$", "");
	}
	
	/**
	 * moves the whole record (line) from the one map to another
	 * @param srcMap - source map
	 * @param dstMap - destination map the record will be moved into
	 * @param entryIndexInSrcMap - record's index in the source map
	 */
	protected static void moveEntryFromMapToMap(Map<CurrentStatusTableField, List<String>> srcMap, Map<CurrentStatusTableField, List<String>> dstMap, int entryIndexInSrcMap) {

		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			List<String> tmpEntryList = new ArrayList<>();
			try {
				if (dstMap.containsKey(statusTableField)) {
					tmpEntryList.addAll(dstMap.get(statusTableField));
				}
				tmpEntryList.add(srcMap.get(statusTableField).remove(entryIndexInSrcMap));
				dstMap.put(statusTableField,tmpEntryList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * looks up for a desired entry in a field of the given map 
	 * @param statusMap - map for lookup
	 * @param currentStatusTableField - field for lookup
	 * @param entry - entry to be found
	 * @return entry's index in an list, or -1 if not found
	 */
	protected static int getIndexInListPerEntry(Map<CurrentStatusTableField, List<String>> statusMap, CurrentStatusTableField currentStatusTableField, String entry) {
		
		int index = -1;
		
		if (entry == null) {
			return -1;	// entry not found
		}
		
		List<String> entryList = statusMap.get(currentStatusTableField);
		for (String lEntry : entryList) {
			index++;
			if (lEntry.equalsIgnoreCase(entry)) {
				return index;
			}
		}
		return -1;	// entry not found
	}
}
