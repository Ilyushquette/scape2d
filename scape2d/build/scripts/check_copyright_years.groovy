import groovy.io.FileType
import java.util.regex.Pattern

def scanYearToken = { yearToken ->
  if(yearToken.contains("-")) {
    def yearsRangeBounds = yearToken.split("-");
    yearsRangeBounds[0] .. yearsRangeBounds[1];
  }else yearToken;
}

def scanYears = { yearsToken ->
  def yearTokens = yearsToken.split(",");
  yearTokens.inject([], { list, yearToken ->
    list + scanYearToken(yearToken)
  });
}

def scanCopyrightYears = { string ->
  def copyrightPattern = Pattern.compile("Copyright \\(c\\) ((\\d{4},|\\d{4}\\-\\d{4},)*(\\d{4}|\\d{4}\\-\\d{4})) (.+)");
  def matcher = copyrightPattern.matcher(string);
  matcher.find() ? scanYears(matcher.group(1)) : [];
}

def license = new File(properties.licensePath);
def lines = license.readLines();
def copyrightYears = lines.collect(scanCopyrightYears).flatten();
def revisionYears = "git log --date=format:\"%Y\" --pretty=format:\"%ad\"".execute().inputStream.readLines().toSet();
def missingYears = revisionYears - copyrightYears;
if(!missingYears.isEmpty()) {
  def error = String.format("Copyright dates need to be updated!%n" +
                            "File ${properties.licensePath} misses copyright years: ${missingYears}%n" +
                            "Copyright format - Copyright (c) YEARS OWNER%n" +
                            "where YEARS - #### (year) or ####-#### (years range) separated by comma (no spaces).%n" +
                            "Multiple copyrights per file are permitted.");
  throw new Exception(error);
}