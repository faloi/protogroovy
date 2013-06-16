package utn.tadp.protogroovy

@Category(Object)
class Prototipable {

	def propertyMissing(String name, value) {
		this.metaClass."get${name.capitalize()}" = { -> value }
	}	
	
}
