package reach52.marketplace.community.aqm.repo

import org.junit.Test

class QuestionsRepoTest {

	@Test
	fun parseJson() {

		val json = """
			
			{
				"questions":[
					
					{
                        "id": "Q1",
                        "question": {
                            "en": "What is your name?",
                            "km": ""
                        },
                        "inputType": "text",
                        "category": "insurance",
                        "condition": null,
                        "range": null,
                        "optional": false
                    },
					{
						"id": "Q2",
                        "question": {
                            "en": "What is gender?",
                            "km": ""
                        },
                        "inputType": "single",
                        "category": "insurance",
                        "condition": null,
                        "range": null,
                        "optional": false,
						"options": [
						
							{
                                "id": "1",
                                "text": {
                                    "en": "Male",
                                    "km": "Male in khmer"
                                }
                            },
                            {
                                "id": "2",
                                "text": {
                                    "en": "",
                                    "km": ""
                                }
                            }
						
						]
						
					}
					
				]
			}
			
		""".trimIndent()

		val qs = QuestionsRepo.parseJson(json)
		println(qs)

	}
}