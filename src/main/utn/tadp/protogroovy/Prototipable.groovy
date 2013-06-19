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
		this.ejecutarEnPrototipo "get${name.capitalize()}", null, { proto -> proto."${name}" }
	}

	def methodMissing(String name, args) {
		this.ejecutar this, name, args
	}
	
	def ejecutar(contexto, mensaje, argumentos) {
		def metodo = this.metodos[mensaje]
		
		if (metodo == null)
			this.ejecutarEnPrototipo mensaje, argumentos, { proto -> proto.ejecutar(this, mensaje, argumentos) }
		else
			contexto.withParams(metodo, argumentos)
	}
	
	def agregarProperty(nombre, valor) {
		this.metaClass."get${nombre.capitalize()}" = { -> valor }
	}
	
	def agregarMetodo(nombre, comportamiento) {
		this.metodos[nombre] = comportamiento
	}
	
	def ejecutarEnPrototipo(mensajeSolicitado, argumentos, bloque) {
		if (this.prototype == null)
			throw new MissingMethodException(mensajeSolicitado, Object.class, argumentos)
			
		bloque this.prototype
	}
}
