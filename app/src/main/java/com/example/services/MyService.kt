package com.example.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.trimSubstring
import java.io.IOException
import kotlin.random.Random




class MyService : Service() {
    //private val handler = Handler()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    //private val downloadBooks = DATA_DOWNLOADED()
    val stopWords = setOf("able","about","above","abroad","according","accordingly","across","actually","adj","after","afterwards","again","against","ago","ahead","ain't","all","allow","allows","almost","alone","along","alongside","already","also","although","always","am","amid","amidst","among","amongst","an","and","another","any","anybody","anyhow","anyone","anything","anyway","anyways","anywhere","apart","appear","appreciate","appropriate","are","aren't","around","as","a's","aside","ask","asking","associated","at","available","away","awfully","back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by","came","can","cannot","cant","can't","caption","cause","causes","certain","certainly","changes","clearly","c'mon","co","co.","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn't","course","c's","currently","dare","daren't","definitely","described","despite","did","didn't","different","directly","do","does","doesn't","doing","done","don't","down","downwards","during","each","edu","eg","eight","eighty","either","else","elsewhere","end","ending","enough","entirely","especially","et","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","fairly","far","farther","few","fewer","fifth","first","five","followed","following","follows","for","forever","former","formerly","forth","forward","found","four","from","further","furthermore","get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings","had","hadn't","half","happens","hardly","has","hasn't","have","haven't","having","he","he'd","he'll","hello","help","hence","her","here","hereafter","hereby","herein","here's","hereupon","hers","herself","he's","hi","him","himself","his","hither","hopefully","how","howbeit","however","hundred","i'd","ie","if","ignored","i'll","i'm","immediate","in","inasmuch","inc","inc.","indeed","indicate","indicated","indicates","inner","inside","insofar","instead","into","inward","is","isn't","it","it'd","it'll","its","it's","itself","i've","just","k","keep","keeps","kept","know","known","knows","last","lately","later","latter","latterly","least","less","lest","let","let's","like","liked","likely","likewise","little","look","looking","looks","low","lower","ltd","made","mainly","make","makes","many","may","maybe","mayn't","me","mean","meantime","meanwhile","merely","might","mightn't","mine","minus","miss","more","moreover","most","mostly","mr","mrs","much","must","mustn't","my","myself","name","namely","nd","near","nearly","necessary","need","needn't","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","no-one","nor","normally","not","nothing","notwithstanding","novel","now","nowhere","obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","one's","only","onto","opposite","or","other","others","otherwise","ought","oughtn't","our","ours","ourselves","out","outside","over","overall","own","particular","particularly","past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides","que","quite","qv","rather","rd","re","really","reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round","said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan't","she","she'd","she'll","she's","should","shouldn't","since","six","so","some","somebody","someday","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","thats","that's","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there'd","therefore","therein","there'll","there're","theres","there's","thereupon","there've","these","they","they'd","they'll","they're","they've","thing","things","think","third","thirty","this","thorough","thoroughly","those","though","three","through","throughout","thru","thus","till","to","together","too","took","toward","towards","tried","tries","truly","try","trying","t's","twice","two","un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","upwards","us","use","used","useful","uses","using","usually","v","value","various","versus","very","via","viz","vs","want","wants","was","wasn't","way","we","we'd","welcome","well","we'll","went","were","we're","weren't","we've","what","whatever","what'll","what's","what've","when","whence","whenever","where","whereafter","whereas","whereby","wherein","where's","whereupon","wherever","whether","which","whichever","while","whilst","whither","who","who'd","whoever","whole","who'll","whom","whomever","who's","whose","why","will","willing","wish","with","within","without","wonder","won't","would","wouldn't","yes","yet","you","you'd","you'll","your","you're","yours","yourself","yourselves","you've","zero","a","how's","i","when's","why's","b","c","d","e","f","g","h","j","l","m","n","o","p","q","r","s","t","u","uucp","w","x","y","z","I","www","amount","bill","bottom","call","computer","con","couldnt","cry","de","describe","detail","due","eleven","empty","fifteen","fifty","fill","find","fire","forty","front","full","give","hasnt","herse","himse","interest","itse”","mill","move","myse”","part","put","show","side","sincere","sixty","system","ten","thick","thin","top","twelve","twenty","abst","accordance","act","added","adopted","affected","affecting","affects","ah","announce","anymore","apparently","approximately","aren","arent","arise","auth","beginning","beginnings","begins","biol","briefly","ca","date","ed","effect","et-al","ff","fix","gave","giving","heres","hes","hid","home","id","im","immediately","importance","important","index","information","invention","itd","keys","kg","km","largely","lets","line","'ll","means","mg","million","ml","mug","na","nay","necessarily","nos","noted","obtain","obtained","omitted","ord","owing","page","pages","poorly","possibly","potentially","pp","predominantly","present","previously","primarily","promptly","proud","quickly","ran","readily","ref","refs","related","research","resulted","resulting","results","run","sec","section","shed","shes","showed","shown","showns","shows","significant","significantly","similar","similarly","slightly","somethan","specifically","state","states","stop","strongly","substantially","successfully","sufficiently","suggest","thered","thereof","therere","thereto","theyd","theyre","thou","thoughh","thousand","throug","til","tip","ts","ups","usefully","usefulness","'ve","vol","vols","wed","whats","wheres","whim","whod","whos","widely","words","world","youd","youre")

    val urls = arrayListOf(
        "https://www.gutenberg.org/cache/epub/11/pg11.txt",
        "https://www.gutenberg.org/cache/epub/84/pg84.txt",
        "https://www.gutenberg.org/cache/epub/2701/pg2701.txt",
        "https://www.gutenberg.org/cache/epub/1513/pg1513.txt",
        "https://www.gutenberg.org/cache/epub/25344/pg25344.txt")



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService ----->","started")
        download()
        return START_STICKY
    }
    private suspend fun getBookHttp(url:String) : String = withContext(Dispatchers.IO){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("error: ${response.code}")
                }
                response.body?.string().orEmpty()
            }
        }
        catch (e: Exception){
            "Exception: ${e.message}"
        }
    }
    private suspend fun awaitForAll(urls: List<String>) : List<String>{
        return coroutineScope{
            urls.map{ url ->
                async {
                    getBookHttp(url)
                }
            }.awaitAll()
        }
    }

    private fun download(){
        coroutineScope.launch {
            try{
                val content = awaitForAll(urls)

                content.forEach(){ book ->
                    val words = book.split("\\s+".toRegex())

                    var wordsWOStopWords = words.filterNot { stopWords.contains(it.lowercase()) }
                    
                    val numByWord = wordsWOStopWords.groupingBy { it }.eachCount()
                    val mostCommon = numByWord.maxByOrNull{ it.value }?.key?.trim(',', '.', '?', '"', '\'') ?: ""

                    val bookInfo  = BookInfo(
                        book.split("\n", ignoreCase = false, limit = 2)[0].substringAfter("The Project Gutenberg eBook of "),
                        book.count { it == ' ' },
                        book.count(),
                        mostCommon)

                    Log.d("MyService ----->","${bookInfo.title}, ${bookInfo.wordCount}, ${bookInfo.charCount}, ${bookInfo.mostCommonWord} ")

                    Log.d("MyService ----->","Book Downloaded")
                    val broadcastIntent = Intent("com.example.services.DATA_DOWNLOADED")

                    broadcastIntent.putExtra("TITLE", bookInfo.title)
                    broadcastIntent.putExtra("WORD_COUNT", bookInfo.wordCount)
                    broadcastIntent.putExtra("CHAR_COUNT", bookInfo.charCount)
                    broadcastIntent.putExtra("COMMON_WORD", bookInfo.mostCommonWord)
                    sendBroadcast(Intent(broadcastIntent))
                }
            }catch (e : Exception){
                Log.d("Download ---->" , "Exception: ${e.message}")
            }finally {
                stopSelf()
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("MyService ----->","onBind()")
        return null
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
        //handler.removeCallbacksAndMessages(null)
        Log.d("MyService ----->","onDestroy()")
    }
}