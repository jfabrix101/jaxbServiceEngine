package it.jaxbservice.util;


import java.util.Map;
import java.util.Properties;


/**
 * Classe per la gestione di Properties con definizioni di costanti
 * e loro sostituzione a run-time. 
**/
public class XProperties extends Properties implements java.io.Serializable {
	
	private static final String START_CONST = "${";
	private static final String END_CONST   = "}";
	
	private static final int MAX_SUBST_DEPTH = 5;
	
	/**
	 * Crea una property list senza valori di default
	 */
	public XProperties() {
		super();
	}
	
	/**
	 *  Importa le definizione di un'altra properties
	 */
	public void copyProperties(Properties source) {
		java.util.Enumeration enum1 = source.keys();
		while (enum1.hasMoreElements()) {
			String key = (String)enum1.nextElement();
			put(key, source.getProperty(key));
		}
	}
	
	/**
	 * Crea una property list con una di default
	 * 
	 * @param defaults java.util.Properties
	 */
	public XProperties(Properties defaults) {
		super(defaults);
	}
	/**
	 * Cerca la property assegnata alla chiave nella property list. 
	 */
	public String getProperty(String key) {
		
		// Restituisce la property a livello 0
		return getProperty(key, 0);
	}
	
	public String getOriginalProperty(String key) {
	
		// Restituisce la property originale (senza sostituzioni in linea)
		return super.getProperty(key);
		
	}
	private String getProperty(String key, int level) {


		String value = super.getProperty(key);
		if (value == null) return null;
		int beginIndex = 0;
		int startName = value.indexOf(START_CONST, beginIndex);
		
		while (startName != -1) {
			if (level+1 > MAX_SUBST_DEPTH) {
				// Supera il livello massimo di annidameno. Restituisce la
				// stringa cos� com'�.
				return value;
			}
			
			int endName = value.indexOf(END_CONST, startName);
			if (endName == -1) {
				// Simbolo di termine costante non trovato. Restituisce il valore
				return value;
			}
			
			String constName = value.substring(startName+START_CONST.length(), endName);
			String constValue = getProperty(constName, level+1);
			
			if (constValue == null) {
				// Property per la costante non trovata
				return value;
			}
			
			// Sostituisce la costante individuata
			String newValue = (startName>0) ? value.substring(0, startName) : "";
			newValue += constValue;
			
			beginIndex = newValue.length();
			newValue += value.substring(endName+1);
			value = newValue;
			
			startName = value.indexOf(START_CONST, beginIndex);			
		}
		
		return value;
	}
	
	
	// Restituisce il valore della property come BOOLEAN
	public boolean getAsBoolean(String key) {
		return new Boolean(getProperty(key)).booleanValue();
	}
	
	// Restituisce il valore della property come INTERO
	public int getAsInteger(String key) {
		int res = 0;
		try {
			res = new Integer(getProperty(key)).intValue();
		} catch (NumberFormatException e ) {
			res = -1;
		}
		return res;
	}
	
	
	public synchronized Object setProperty(String key, String val) {
		if (key == null) return super.setProperty(key, "__NULL__");
		else return super.setProperty(key, val);
	}
	
	/*
	 * Previene eventuali NullPointerException.
	 */
	@Override
	public synchronized Object put(Object key, Object value) {
		if (key == null || value == null) {
			System.err.println("[XProperties] [WARNING] - Trying to save null value in XProperties: Key = " + key + ", value = " + value);
			return null;
		} else return super.put(key, value);
	}
	
	public static XProperties importFromMap(Map<String, String> map) {
		XProperties res = new XProperties();
		for(Map.Entry<String, String> item : map.entrySet()) {
			res.put(item.getKey(), item.getValue());
		}
		return res;
	}
}
