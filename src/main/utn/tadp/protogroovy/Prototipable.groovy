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
		this.validarPrototipo "get${name.capitalize()}", null
		this.prototype."${name}"
	}

	def methodMissing(String name, args) {
		this.ejecutar this, name, args
	}
	
	def ejecutar(contexto, mensaje, argumentos) {
		def metodo = this.metodos[mensaje]
		
		if (metodo == null)
			this.ejecutarEnPrototipo mensaje, argumentos
		else
			contexto.withParams(metodo, argumentos)
	}
	
	def ejecutarEnPrototipo(mensaje, argumentos) {
		this.validarPrototipo mensaje, argumentos
		this.prototype.ejecutar this, mensaje, argumentos
	}
	
	def agregarProperty(nombre, valor) {
		this.metaClass."get${nombre.capitalize()}" = { -> valor }
	}
	
	def agregarMetodo(nombre, comportamiento) {
		this.metodos[nombre] = comportamiento
	}
	
	def validarPrototipo(mensajeSolicitado, argumentos) {
		if (this.prototype == null)
			throw new MissingMethodException(mensajeSolicitado, Object.class, argumentos)
	}
}
