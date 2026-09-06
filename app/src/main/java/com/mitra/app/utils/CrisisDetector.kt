package com.mitra.app.utils

/**
 * Port of the web app's CRISIS_PHRASES + CRISIS_REGEX lists.
 * Kept as a pure utility object — no Android deps — for easy unit-testing.
 */
object CrisisDetector {

    private val PHRASES = listOf(
        "kill myself","killing myself","want to die","wanna die","don't want to live",
        "dont want to live","not want to live","don't want to be alive","dont want to be alive",
        "end my life","end it all","end it tonight","no reason to live","no point in living",
        "better off dead","wish i was dead","wish i were dead","want to disappear",
        "suicide","suicidal","hurt myself","harm myself","cut myself","cutting myself",
        "can't go on","cant go on","can't do this anymore","cant do this anymore",
        "can't take it anymore","cant take it anymore","give up on life",
        "don't want to be here","dont want to be here","want it to end","i want to end",
        "don't want to exist","dont want to exist","not worth living",
        // Hindi / Hinglish
        "marna hai","marna chah","mar jana chah","mar jaana chah","marne ki wish",
        "mar jau","mar jaun","mar jaunga","mar jaungi","mar jana","mar jaana",
        "marne ka mann","marne ka man","khud ko khatam","apne aap ko khatam","khatam kar du",
        "khatam kar dun","khatam kr","zindagi khatam","sab khatam","zindagi se tang",
        "jeena nahi","jeena nhi","jeene ka mann nahi","jeene ka man nahi","nahi jeena",
        "nhi jeena","jina nahi","jina nhi","jee nahi paunga","jee nhi paunga",
        "jee nahi paungi","jee nhi paungi","ji nahi paunga","ji nhi paunga",
        "nahi rahunga","nhi rahunga","nahi rah paunga","jaan de","jaan dena",
        "jaan de dunga","jaan de dungi","khudkushi","atmahatya",
        "jeene ki khwaish nahi","jeene ki khwaish nhi","jeene ki iccha nahi",
        "jeene ki ichha nahi","jeene ki wajah nahi","jeene ka koi matlab nahi",
        "jeene ka koi maksad nahi","zindagi jeene ki khwaish","zindgi jeene ki khwaish",
        "jeene ki khwahish","kya faayda jeene","kya fayda jeene","faayda ab jeene",
        "fayda ab jeene","not being here anymore","not be here anymore",
        "wasn't here anymore","wasnt here anymore","bina jee","bina jiye",
        "bina reh nahi","khatam ho jau","mit jau","mit jana",
        // Adversarial / euphemistic
        "want everything to stop","everything to just stop",
        "not be a problem for anyone","not be a burden anymore",
        "relieved if i disappeared","relieved if i wasn't here",
        "tired of existing","so tired of existing",
        "not wake up tomorrow","not waking up tomorrow",
        "better if i wasn't here","better if i wasn't around",
        "sabke liye bojh","bojh hoon sabke liye","main na rahoon","main na rahu",
        "giving away my stuff","giving away my things","wrote letters","just in case letters",
        "decided how i'm doing it","decided how im doing it",
        "plan kar liya hai","sab kuch plan kar liya",
        "bahut ho gaya","ab nahi jiya jaata","nahi jiya jata",
        "khatam kar doon khud","khatam kar doon apne aap",
        "mann bhar gaya zindagi se","zindagi se mann bhar gaya",
        "last raasta bacha hai","yehi last raasta","nahi seh sakta","nahi sah sakta",
        "gayab ho jana hamesha","hamesha ke liye gayab",
        "thinking about ending things","ending things, no big deal",
        "planning my own funeral","already a ghost","feel like a ghost walking",
        "khokla ho chuka hoon","andar se khokla","fade out of the picture",
        "fading out of the picture","notice if i wasn't here","notice if i wasnt here",
        "kal na uthoon","na uthoon apni neend se","stop existing",
        "people get better after they stop existing"
    )

    private val REGEX_PATTERNS = listOf(
        Regex("""bina\s+.{0,25}(jee|ji|jeena|reh|rah|jaun|paun|jiy)\w*\s*.{0,12}(nahi|nhi|nai)\b"""),
        Regex("""(jee|ji|jeena|jina|jiun|jiunga|paunga|paungi|rahunga|rahungi)\s+.{0,8}(nahi|nhi|nai)\b"""),
        Regex("""(khud|apne\s?aap)\s+ko\s+.{0,15}(khatam|maar|marna|nuksan)"""),
        Regex("""jeene?\s+k[iea]\s+.{0,20}(khwaish|khwahish|iccha|ichha|wajah|matlab|maksad|man|mann)\s*.{0,10}(nahi|nhi|nai)\b"""),
        Regex("""kh(a)?tm\w*\s*.{0,8}(kar|kr)\s*.{0,8}(du|de|dena|dunga|dungi|doonga|doongi)\b"""),
        Regex("""(relieved|better)\s+if\s+.{0,15}(wasn'?t|wasnt)\s+(here|around)"""),
        Regex("""(relieved|better)\s+if\s+.{0,15}disappear"""),
        Regex("""(would\s?n'?t|would)\s+.{0,25}notice\s+if\s+i\s+.{0,10}(wasn'?t|wasnt)"""),
        Regex("""(wasn'?t|isn'?t|not\s+be)\s+a\s+problem\s+for\s+anyone"""),
        Regex("""giving\s+away\s+(my\s+)?(stuff|things|belongings)"""),
        Regex("""wrote\s+letters?\s+.{0,15}(just in case|goodbye|parents)"""),
        Regex("""decided\s+how\s+.{0,10}(doing it|do it|end it)"""),
        Regex("""bahut\s+ho\s+gaya.{0,20}(nahi|nhi)\s+jiy?a\s+jaata"""),
        Regex("""(nahi|nhi)\s+seh\s+sakta.{0,20}khatam"""),
        Regex("""zindagi\s+se.{0,15}mann\s+bhar\s+gaya"""),
        Regex("""mann\s+bhar\s+gaya.{0,15}zindagi"""),
        Regex("""gayab\s+ho\s+jana.{0,15}hamesha"""),
        Regex("""better\s+lagega\s+agar.{0,20}(wasn'?t|wasnt)\s+around"""),
        Regex("""(already\s+a\s+ghost|feel\s+like\s+a\s+ghost)"""),
        Regex("""khokla\s+ho\s+chuka\s+hoon"""),
        Regex("""fad(e|ing)\s+out\s+of\s+the\s+picture"""),
        Regex("""tired\s+of\s+existing"""),
        Regex("""not\s+wak(e|ing)\s+up\s+tomorrow"""),
        Regex("""(kal\s+)?na\s+uthoon\s+.{0,15}neend""")
    )

    fun isCrisis(text: String): Boolean {
        val t = text.lowercase().replace(Regex("""\s+"""), " ")
        if (PHRASES.any { t.contains(it) }) return true
        return REGEX_PATTERNS.any { it.containsMatchIn(t) }
    }
}
