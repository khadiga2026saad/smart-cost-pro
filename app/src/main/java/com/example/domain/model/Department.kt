package com.example.domain.model

enum class Department(val id: String, val titleAr: String, val defaultCategories: List<String>) {
    PERSONAL(
        "PERSONAL",
        "المصروفات الشخصية",
        listOf("عناية شخصية", "ملابس وأناقة", "مطاعم وكافيهات", "ترفيه وهوايات", "اشتراكات شخصية", "صحة وأدوية", "أخرى")
    ),
    HOME(
        "HOME",
        "مصروفات المنزل",
        listOf("تموين ومستلزمات", "فواتير ومرافق", "صيانة وأجهزة", "إيجار / أقساط", "أثاث وديكور", "أخرى")
    ),
    EDUCATION(
        "EDUCATION",
        "مصروفات التعليم",
        listOf("رسوم دراسية", "كتب ومراجع", "دروس ودورات", "أدوات وقرطاسية", "أنشطة مدرسية", "أخرى")
    ),
    WORK(
        "WORK",
        "مصروفات العمل",
        listOf("معدات ومكتب", "اشتراكات برمجيات", "تنقلات وسفر عمل", "تسويق وإعلانات", "استشارات مهنية", "أخرى")
    ),
    VEHICLE(
        "VEHICLE",
        "مصروفات المركبات",
        listOf("وقود وبنزين", "صيانة دورية", "قطع غيار وإطارات", "تأمين وتراخيص", "غسيل ومواقف", "أخرى")
    );

    companion object {
        fun fromId(id: String?): Department {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: PERSONAL
        }
    }
}
