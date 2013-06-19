package utn.tadp.protogroovy

@Category(Object)
class Prototipable {

	def metodos = [:]
	def prototype
	
	def propertyMissing(String name, value) {
		if (value instanceof Closure)
			this.agregarMetodo name, value
		else
			this.agregarProperty name, value
	}	
	
	def propertyMissing(String name) {
		if (this.prototype == null)
			throw new MissingMethodException("get${name.capitalize()}", Object.class, null)
		
		this.prototype."${name}"
	}

	def methodMissing(String name, args) {
		def metodo = this.metodos[name]
		
		if (metodo == null)
			throw new MissingMethodException(name, Object.class, args)
			
		this.withParams(metodo, args)
	}
	
	def agregarProperty(nombre, valor) {
		this.metaClass."get${nombre.capitalize()}" = { -> valor }
	}
	
	def agregarMetodo(nombre, comportamiento) {
		this.metodos[nombre] = comportamiento
	}
}
