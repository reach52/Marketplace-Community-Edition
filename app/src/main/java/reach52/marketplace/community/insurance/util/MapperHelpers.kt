package reach52.marketplace.community.insurance.util

fun getLangText(properties: Map<String, Any>, key: String, lang: String, def: String = ""): String {
	if (!properties.containsKey(key)) {
		return def
	}
	val value = properties[key]
	if(value is String){
		return value
	}
	val map = (value as Map<String, String>)
	return getLangTextFromMap(map, lang)
}

fun getLangList(properties: Map<String, Any>, key: String, lang: String): List<String> {
	val value = properties[key] as Map<String, Any>

	if(value.containsKey(key)){
		return when {
			value.containsKey(lang.toUpperCase()) -> {
				value[lang.toUpperCase()] as List<String>
			}
			value.containsKey(lang.toLowerCase()) -> {
				value[lang.toLowerCase()] as List<String>
			}
			else -> {
				value["ENG"] as List<String>
			}
		}
	}
	return listOf()
}

fun getLangTextFromMap(map: Map<String, String>, lang: String): String {
	return when {
		map.containsKey(lang.toUpperCase()) -> {
			map[lang.toUpperCase()] as String
		}
		map.containsKey(lang.toLowerCase()) -> {
			try {
				map[lang.toLowerCase()] as String
			} catch (cce: java.lang.ClassCastException) {
				map[lang.toLowerCase()].toString()
			}
		}
		else -> {
			getLangTextFromMap(map, "eng")
		}
	}
}

fun getInt(properties: Map<String, Any>, key: String, def: Int = 0): Int{
	if (properties.containsKey(key)) {
		return try {
			properties[key] as Int
		} catch (e: Exception) {
			return def
		}
	} else {
		return def
	}
}

fun getFloat(properties: Map<String, Any>, key: String, def: Float = 0F): Float{

	if (properties.containsKey(key)) {

		return try {
			properties[key] as Float
		} catch (e: ClassCastException) {
			try {
				(properties[key] as Double).toFloat()
			} catch (e: ClassCastException) {
				try {
					(properties[key] as Int).toFloat()
				} catch (e: ClassCastException) {
					try {
						(properties[key] as String).toFloat()
					} catch (e: Exception) {
						return def
					}
				}
			}
		}

	} else {
		return def
	}
}

fun getDouble(properties: Map<String, Any>, key: String, def: Double = 0.0) : Double{

	return if (properties.containsKey(key)) {
		try {
			(properties[key] as Double)
		} catch (e: Exception) {
			getFloat(properties, key, def.toFloat()).toDouble()
		}
	} else {
		def
	}
}

fun getString(properties: Map<String, Any>, key: String, def: String = ""): String {
	return if (properties.containsKey(key)) {
		try {
			properties[key] as String
		} catch (e: Exception) {
			def
		}
	} else {
		def
	}
}
