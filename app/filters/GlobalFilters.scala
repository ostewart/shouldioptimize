package filters

import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.gzip.GzipFilter

class GlobalFilters @Inject()(gzip: GzipFilter) extends HttpFilters {
  def filters = Seq(gzip)
}
