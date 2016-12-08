[1mdiff --git a/MyApplication/.idea/.name b/MyApplication/.idea/.name[m
[1mindex 68b9646..1046d98 100644[m
[1m--- a/MyApplication/.idea/.name[m
[1m+++ b/MyApplication/.idea/.name[m
[36m@@ -1 +1 @@[m
[31m-Hnnnn[m
\ No newline at end of file[m
[32m+[m[32mMyApplication[m
\ No newline at end of file[m
[1mdiff --git a/MyApplication/.idea/dictionaries/Administrator.xml b/MyApplication/.idea/dictionaries/Administrator.xml[m
[1mdeleted file mode 100644[m
[1mindex fadc5c8..0000000[m
[1m--- a/MyApplication/.idea/dictionaries/Administrator.xml[m
[1m+++ /dev/null[m
[36m@@ -1,7 +0,0 @@[m
[31m-<component name="ProjectDictionaryState">[m
[31m-  <dictionary name="Administrator">[m
[31m-    <words>[m
[31m-      <w>xiangyase</w>[m
[31m-    </words>[m
[31m-  </dictionary>[m
[31m-</component>[m
\ No newline at end of file[m
[1mdiff --git a/MyApplication/.idea/inspectionProfiles/Project_Default.xml b/MyApplication/.idea/inspectionProfiles/Project_Default.xml[m
[1mdeleted file mode 100644[m
[1mindex 856e31c..0000000[m
[1m--- a/MyApplication/.idea/inspectionProfiles/Project_Default.xml[m
[1m+++ /dev/null[m
[36m@@ -1,11 +0,0 @@[m
[31m-<component name="InspectionProjectProfileManager">[m
[31m-  <profile version="1.0">[m
[31m-    <option name="myName" value="Project Default" />[m
[31m-    <inspection_tool class="AndroidLintDuplicateActivity" enabled="false" level="ERROR" enabled_by_default="false" />[m
[31m-    <inspection_tool class="AndroidLintDuplicateDefinition" enabled="false" level="ERROR" enabled_by_default="false" />[m
[31m-    <inspection_tool class="LoggerInitializedWithForeignClass" enabled="false" level="WARNING" enabled_by_default="false">[m
[31m-      <option name="loggerClassName" value="org.apache.log4j.Logger,org.slf4j.LoggerFactory,org.apache.commons.logging.LogFactory,java.util.logging.Logger" />[m
[31m-      <option name="loggerFactoryMethodName" value="getLogger,getLogger,getLog,getLogger" />[m
[31m-    </inspection_tool>[m
[31m-  </profile>[m
[31m-</component>[m
\ No newline at end of file[m
[1mdiff --git a/MyApplication/.idea/inspectionProfiles/profiles_settings.xml b/MyApplication/.idea/inspectionProfiles/profiles_settings.xml[m
[1mdeleted file mode 100644[m
[1mindex 3b31283..0000000[m
[1m--- a/MyApplication/.idea/inspectionProfiles/profiles_settings.xml[m
[1m+++ /dev/null[m
[36m@@ -1,7 +0,0 @@[m
[31m-<component name="InspectionProjectProfileManager">[m
[31m-  <settings>[m
[31m-    <option name="PROJECT_PROFILE" value="Project Default" />[m
[31m-    <option name="USE_PROJECT_PROFILE" value="true" />[m
[31m-    <version value="1.0" />[m
[31m-  </settings>[m
[31m-</component>[m
\ No newline at end of file[m
[1mdiff --git a/MyApplication/.idea/misc.xml b/MyApplication/.idea/misc.xml[m
[1mindex fbb6828..7158618 100644[m
[1m--- a/MyApplication/.idea/misc.xml[m
[1m+++ b/MyApplication/.idea/misc.xml[m
[36m@@ -37,10 +37,26 @@[m
     <ConfirmationsSetting value="0" id="Add" />[m
     <ConfirmationsSetting value="0" id="Remove" />[m
   </component>[m
[31m-  <component name="ProjectRootManager" version="2" languageLevel="JDK_1_8" default="true" assert-keyword="true" jdk-15="true" project-jdk-name="1.8" project-jdk-type="JavaSDK">[m
[32m+[m[32m  <component name="ProjectRootManager" version="2" languageLevel="JDK_1_7" default="true" assert-keyword="true" jdk-15="true" project-jdk-name="1.8" project-jdk-type="JavaSDK">[m
     <output url="file://$PROJECT_DIR$/build/classes" />[m
   </component>[m
   <component name="ProjectType">[m
     <option name="id" value="Android" />[m
   </component>[m
[32m+[m[32m  <component name="masterDetails">[m
[32m+[m[32m    <states>[m
[32m+[m[32m      <state key="ProjectJDKs.UI">[m
[32m+[m[32m        <settings>[m
[32m+[m[32m          <last-edited>1.8</last-edited>[m
[32m+[m[32m          <splitter-proportions>[m
[32m+[m[32m            <option name="proportions">[m
[32m+[m[32m              <list>[m
[32m+[m[32m                <option value="0.2" />[m
[32m+[m[32m              </list>[m
[32m+[m[32m            </option>[m
[32m+[m[32m          </splitter-proportions>[m
[32m+[m[32m        </settings>[m
[32m+[m[32m      </state>[m
[32m+[m[32m    </states>[m
[32m+[m[32m  </component>[m
 </project>[m
\ No newline at end of file[m
[1mdiff --git a/MyApplication/thpms/src/main/java/thp/csii/com/BaseActivity.java b/MyApplication/thpms/src/main/java/thp/csii/com/BaseActivity.java[m
[1mindex 353601d..aa54a76 100644[m
[1m--- a/MyApplication/thpms/src/main/java/thp/csii/com/BaseActivity.java[m
[1m+++ b/MyApplication/thpms/src/main/java/thp/csii/com/BaseActivity.java[m
[36m@@ -51,6 +51,7 @@[m [mpublic class BaseActivity extends AppCompatActivity {[m
     protected void onCreate(Bundle savedInstanceState) {[m
         super.onCreate(savedInstanceState);[m
         super.setContentView(R.layout.activity_base);[m
[32m+[m[32m        //push test[m
 [m
 [m
         findView();[m
warning: LF will be replaced by CRLF in MyApplication/.idea/misc.xml.
The file will have its original line endings in your working directory.
