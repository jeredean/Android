package com.example.uhc

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val sex: String,
    val patientNumber: String,
    val location: String,
    val bloodGroup: String,
    val allergies: String,
    val conditions: List<String>,
    val visits: List<Visit>
)

data class Visit(
    val date: String,
    val facility: String,
    val summary: String
)

object PatientRepository {
    val patients = listOf(
        Patient(
            id = "uhc-24018",
            name = "Amina Nakato",
            age = 34,
            sex = "Female",
            patientNumber = "UHC-24018",
            location = "Mbarara District",
            bloodGroup = "O+",
            allergies = "No known allergies",
            conditions = listOf("Hypertension"),
            visits = listOf(
                Visit("12 Oct 2026", "Mbarara Regional Referral Hospital", "Blood pressure reviewed; continue current medication."),
                Visit("18 Aug 2026", "Kakoba Health Centre IV", "Routine follow-up. Medication adherence discussed.")
            )
        ),
        Patient(
            id = "uhc-19342",
            name = "Peter Okello",
            age = 52,
            sex = "Male",
            patientNumber = "UHC-19342",
            location = "Gulu District",
            bloodGroup = "A+",
            allergies = "Penicillin",
            conditions = listOf("Type 2 diabetes", "Hypertension"),
            visits = listOf(
                Visit("29 Sep 2026", "Gulu Regional Referral Hospital", "Diabetes review. Follow-up planned in four weeks."),
                Visit("03 Jul 2026", "Pece Health Centre III", "Blood glucose checked and treatment plan updated.")
            )
        ),
        Patient(
            id = "uhc-30571",
            name = "Grace Atim",
            age = 27,
            sex = "Female",
            patientNumber = "UHC-30571",
            location = "Lira District",
            bloodGroup = "B+",
            allergies = "No known allergies",
            conditions = listOf("Antenatal care"),
            visits = listOf(
                Visit("04 Nov 2026", "Lira Regional Referral Hospital", "Antenatal visit. Routine assessment completed."),
                Visit("07 Oct 2026", "Ober Health Centre IV", "First antenatal visit recorded.")
            )
        ),
        Patient(
            id = "uhc-41806",
            name = "Moses Ssemanda",
            age = 8,
            sex = "Male",
            patientNumber = "UHC-41806",
            location = "Wakiso District",
            bloodGroup = "AB+",
            allergies = "No known allergies",
            conditions = listOf("Asthma"),
            visits = listOf(
                Visit("21 Oct 2026", "Entebbe Regional Referral Hospital", "Asthma symptoms reviewed; caregiver given follow-up guidance."),
                Visit("10 Jun 2026", "Kajjansi Health Centre III", "Routine review and inhaler technique checked.")
            )
        )
    )

    fun findById(id: String?): Patient? = patients.firstOrNull { it.id == id }
}
