package utn.tadp.protogroovy
import org.junit.*

class PrototipableTest {

	@Before
	void setUp() {
		Object.mixin(Prototipable)
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
		
		a.sumar = { -> numero1 + numero2 }
		
		assert a.sumar() == 8
	}
	
}