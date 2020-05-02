package com.za.caretogether.response
import com.google.gson.annotations.SerializedName


data class GlobalDataResponse(
    @SerializedName("data")
    val `data`: List<GlobalData>
)

data class GlobalData(
    @SerializedName("active_case")
    val activeCase: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("new_cases")
    val newCases: Int,
    @SerializedName("no")
    val no: Int,
    @SerializedName("serious_case")
    val seriousCase: Int,
    @SerializedName("total_cases")
    val totalCases: Int,
    @SerializedName("total_deaths")
    val totalDeaths: Int,
    @SerializedName("new_deaths")
    val new_death: Int,
    @SerializedName("total_recovered")
    val totalRecovered: Int
)

data class ChartResponse(
    @SerializedName("age_chart")
    val ageChart: List<AgeChart>,
    @SerializedName("daily_line_chart")
    val dailyLineChart: DailyLineChart,
    @SerializedName("gender_chart")
    val genderChart: List<GenderChart>,
    @SerializedName("negative_confirm_chart")
    val negative_confirm_chart: List<NegativeConfirmChart>,
    @SerializedName("recover_deaths_chart")
    val recover_deaths_chart: List<RecoverDeathChart>
)

data class AgeChart(
    @SerializedName("total")
    val total: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("percentage")
    val percentage: Float
)

data class NegativeConfirmChart(
    @SerializedName("title")
    val title: String,
    @SerializedName("percentage")
    val percentage: Float
)

data class RecoverDeathChart(
    @SerializedName("title")
    val title: String,
    @SerializedName("percentage")
    val percentage: Float
)

data class DailyLineChart(
    @SerializedName("x_data")
    val xData: List<String>,
    @SerializedName("y_data")
    val yData: List<YData>
)

data class YData(
    @SerializedName("data")
    val `data`: List<String>,
    @SerializedName("title")
    val title: String
)

data class GenderChart(
    @SerializedName("total")
    val total: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("percentage")
    val percentage: Float
)
data class MyanmarSituationResponse(
    @SerializedName("data_by_regions")
    val dataByRegions: List<DataByRegion>,
    @SerializedName("overall_counts")
    val overallCounts: OverallCounts,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class VideoResponse(
    @SerializedName("data")
    val `data`: List<VideoData>
)

data class VideoData(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)

data class DataByRegion(
    @SerializedName("no")
    val no: Int,
    @SerializedName("confirmed")
    val confirmed: String,
    @SerializedName("negative")
    val negative: String,
    @SerializedName("pending")
    val pending: String,
    @SerializedName("death")
    val death: String,
    @SerializedName("quarantined")
    val quarantined: String,
    @SerializedName("quarantined_suspected")
    val quarantinedSuspected: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("suspected")
    val suspected: String
)

data class MyanmarDataResponse(
    @SerializedName("data_by_hospitals")
    val dataByHospitals: List<DataByHospital>,
    @SerializedName("overall_counts")
    val overallCounts: OverallCounts
)

data class FirebaseResponse(
    @SerializedName("phone")
    val user_phone: String
)

data class DataByHospital(
    @SerializedName("no")
    val no: Int,
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("extra")
    val extra: Extra,
    @SerializedName("hospital")
    val hospital: String,
    @SerializedName("negative")
    val negative: Int,
    @SerializedName("pending")
    val pending: Int,
    @SerializedName("positions")
    val positions: Positions,
    @SerializedName("quarantined")
    val quarantined: Int,
    @SerializedName("quarantined_suspected")
    val quarantinedSuspected: Int,
    @SerializedName("region")
    val region: String,
    @SerializedName("suspected")
    val suspected: Int,
    @SerializedName("township")
    val township: String
)

data class Extra(
    @SerializedName("adult")
    val adult: Int,
    @SerializedName("child")
    val child: Int,
    @SerializedName("female")
    val female: Int,
    @SerializedName("male")
    val male: Int
)

data class Positions(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)

data class OverallCounts(
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("dc")
    val dc: Int,
    @SerializedName("negative")
    val negative: Int,
    @SerializedName("pending")
    val pending: Int,
    @SerializedName("suspected")
    val suspected: Int,
    @SerializedName("total_quarantined")
    val totalQuarantined: Int,
    @SerializedName("active_case")
    val active_case: Int,
    @SerializedName("total_quarantined_suspected")
    val totalQuarantinedSuspected: Int,

    @SerializedName("deaths")
    val deaths : Int,

    @SerializedName("recovered")
    val recovered : Int
)

data class CountForMeResponse(
    @SerializedName("count")
    val count: Int
)

data class SettingResponse(
    @SerializedName("local_list")
    val localList: List<Local>,
    @SerializedName("locals")
    val locals: List<LocalX>,
    @SerializedName("ministry_on")
    val ministryOn: Boolean,
    @SerializedName("ministry_on_test")
    val ministryOnTest: Boolean
)

data class Local(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("name")
    val name: String
)

data class LocalX(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("name")
    val name: String
)

data class UpdateResponse(
    @SerializedName("version")
    val version: Int,
    @SerializedName("version_code")
    val version_code: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("updated_note")
    val updated_note: String,
    @SerializedName("need_update")
    val need_update: Boolean,
    @SerializedName("playstore")
    val playstore: Boolean

)

data class NewsResponse(
    @SerializedName("data")
    val `data`: List<NewsData>,
    @SerializedName("links")
    val links: Links,
    @SerializedName("meta")
    val meta: Meta
)


data class Links(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("next")
    val next: String
)

data class Meta(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
)

data class NewsData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_today_news")
    val is_today_news: Boolean,
    @SerializedName("post_date")
    val postDate: String,
    @SerializedName("title")
    val title: String
)

data class NewsDetailResponse(
    @SerializedName("data")
    val `data`: NewsDetailData
)

data class NewsDetailData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("link")
    val link: String,
    @SerializedName("paragraphs")
    val paragraphs: List<String>,
    @SerializedName("post_date")
    val postDate: String,
    @SerializedName("title")
    val title: String
)

data class ContactResponse(
    @SerializedName("keys")
    val keys: List<String>,
    @SerializedName("values")
    val values: List<Value>
)


data class Value(
    @SerializedName("datas")
    val datas: List<ContactByRegion>,
    @SerializedName("region_en")
    val regionEn: String
)

data class ContactByRegion(
    @SerializedName("duty_department")
    val dutyDepartment: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phones")
    val phones: List<String>,
    @SerializedName("role")
    val role: String,
    @SerializedName("township")
    val township: String
)
