package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Dependent
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.threeten.bp.ZonedDateTime

class DependentManagerTest {

	@Before
	fun setUp() {



	}

	@Test
	fun validate_no_name_dependent(){

		val dependent = Dependent("",ZonedDateTime.now(), "", "")


		val to = TestObserver<Boolean>()
		DependentManager.validateDependent(dependent).subscribe(to)
		to.assertError(DependentManager.EmptyNameException::class.java)

	}

	@Test
	fun validate_minor_dependent(){

		val dependent = Dependent("Hello WOrld", ZonedDateTime.now(), "", "")


		val to = TestObserver<Boolean>()
		DependentManager.validateDependent(dependent).subscribe(to)
		to.assertError(DependentManager.AgeTooLowException::class.java)

	}

	@Test
	fun validate_valid_dependent(){

		val date = ZonedDateTime.parse("1984-02-03T00:00:00+01:00")
		val dependent = Dependent("Hello World",date, "Male", "")


		val to = TestObserver<Boolean>()
		DependentManager.validateDependent(dependent).subscribe(to)
		to.assertValue(true)

	}

}