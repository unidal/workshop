set repo_id=thirdparty
set repo_url=http://tech-wuqim/nexus/content/repositories/thirdparty/

echo "deploy jars"

call mvn -N deploy:deploy-file -DrepositoryId=%repo_id% -Durl=%repo_url% -DgeneratePom=true -DgroupId=com.lowagie -DartifactId=itext-asian -Dversion=1.0 -Dpackaging=jar -Dfile=itext-asian-1.0.jar

echo "deploy source jars"

rem call mvn -N deploy:deploy-file -DrepositoryId=%repo_id% -Durl=%repo_url% -DgeneratePom=false -DgroupId=com.ebay -DartifactId=com.ebay.kernel -Dversion=1.0.0 -Dpackaging=jar -Dclassifier=sources -Dfile=%lib_dir%\sources\com.ebay.kernel-sources-1.0.0.jar
