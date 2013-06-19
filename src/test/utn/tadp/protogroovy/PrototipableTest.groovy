package utn.tadp.protogroovy
import org.junit.*

class PrototipableTest {

	@Before
	void setUp() {
		Object.mixin(Prototipable)
		
		Object.metaClass {
			withParams = { closure, Object... params ->
				final Closure clonedClosure = closure.clone();
				clonedClosure.setResolveStrategy(Closure.DELEGATE_FIRST);
				clonedClosure.setDelegate(delegate);
				clonedClosure.call(*params);
			}
		}
	}
	
	@Test
	void "puede agregarse una property y posteriormente obtenerla"() {
		def a = new Object()
		a.numero1 = 3
		
		assert a.numero1 == 3
	}
	
	@Test
	void "puede agregarse un metodo sin parametros y posteriormente ejecutarlo"() {
		def a = new Object()
		a.saludar = { -> "hola mundo!" }
		
		assert a.saludar() == "hola mundo!"
	}
	
	@Test
	void "puede agregarse un metodo con parametros y posteriormente ejecutarlo"() {
		def a = new Object()
		a.saludarA = { unNombre -> "hola ${unNombre}!" }
		
		assert a.saludarA("don pepito") == "hola don pepito!"
	}
	
	@Test
	void "la ejecucion de un metodo se realiza en el contexto del objeto"() {
		def a = new Object()
		a.numero1 = 5
		a.numero2 = 3
		
		a.sumar = { numero3, numero4 -> numero1 + numero2 + numero3 + numero4 }
		
		assert a.sumar(10, 20) == 38
	}
	
	@Test
	void "si el objeto no tiene una property se busca en su prototipo"() {
		def a = new Object()
		a.nombre = "don jose"
		
		def b = new Object()
		b.prototype = a
		
		assert b.nombre == "don jose"
	}
	
	@Test(expected=MissingMethodException.class)
	void "si el prototipo no tiene la property arroja MissingMethodException"() {
		def a = new Object()
		a.nombre = "don jose"
		
		def b = new Object()
		b.prototype = a
		
		assert b.edad == 21
	}
	
	@Test
	void "si el objeto no tiene un metodo se busca en su prototipo"() {
		def a = new Object()
		a.numero1 = 5
		a.numero2 = 3
		
		a.sumar = { numero3, numero4 -> numero1 + numero2 + numero3 + numero4 }
		
		def b = new Object()
		b.prototype = a
		
		b.numero2 = 9
		
		assert b.sumar(10, 20) == 44
	}
	
	@Test(expected=MissingMethodException.class)
	void "si ni el objeto ni su prototipo entienden el mensaje arroja MissingMethodException"() {
		def a = new Object()
		a.saludar = { -> "hola amigo!" }
		
		def b = new Object()
		b.prototype = a
		
		assert b.despedir() == "chau amigo!"
	}
}