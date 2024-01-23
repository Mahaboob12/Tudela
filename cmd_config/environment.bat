REM
REM Smallworld Product Windows Environment
REM
REM This file should not normally need to be edited by hand.
REM In any case, do not add anything other than simple SET, REM and
REM CALL lines, as it is read by the 'gis' command, not just by cmd.exe.
REM Note that the CALL statement can be used to call other batch files.
REM However these have the same restrictions on their contents. Also
REM the gis launcher program limits the CALL stack to be a maximum of 32
REM levels deep.
REM

set SW_PRODUCTS_PATH=
set SW_MESSAGE_DB_DIR=\\iberlanoff.dynu.net\Smallworld410\product\data
set SW_GIS_PATTERN_DIR=%SMALLWORLD_GIS%\data\xview_patterns 
set SW_FONT_PATH=%SMALLWORLD_GIS%\data\vecfonts
set SW_FONT_CONFIG=%SMALLWORLD_GIS%\config\font\custom;%SMALLWORLD_GIS%\config\font
set SW_FONT_METRICS=%SMALLWORLD_GIS%\config\fontmetrics
set SW_CODE_TABLES=%SMALLWORLD_GIS%\data\code_tables
set SW_GIS_TEMPLATE_DIR=%SMALLWORLD_GIS%\data\template
set SW_GIS_DEFAULT_STYLES_DIR=%SMALLWORLD_GIS%\data\template
set SW_GIS_GLAZIER_DIR=%SMALLWORLD_GIS%\data\glazier
set SW_GIS_DOC_DIR=%SMALLWORLD_GIS%\data\doc
set SW_GIS_PLOT_FILTER_DIR=%SMALLWORLD_GIS%\plotters\site_specific
set SW_MDB_TRANSPORTS=tcpip
set SW_MDB_KEEPALIVE=60,10
set SW_COMPONENT_PATH=%SMALLWORLD_GIS%\sw_core\source
set SW_ACP_PATH=%SMALLWORLD_GIS%\etc\%PROCESSOR_ARCHITECTURE%;%SMALLWORLD_GIS%\etc\x86
set PATH=%SMALLWORLD_GIS%\bin\share;%SMALLWORLD_GIS%\bin\%PROCESSOR_ARCHITECTURE%;%SMALLWORLD_GIS%\bin\x86;%SMALLWORLD_GIS%\3rd_party_libs\WNT.x86\bin;%PATH%
set EMACSROOT=%SMALLWORLD_GIS%\..\emacs
set SW_COMMON_PROGRAM_GROUP=Smallworld430


rem ================================================================
rem =  Personalizaciones en el entorno de Iberlan, TUDELA
rem ================================================================

rem set TUDELA = \\Servidorgis\c\SWG\Aguas_Tudela43
rem set SW_SAVE_IMAGE_DIR=%TUDELA%\images
rem set TUDELA_DB_DIR =\\Servidorgis\c\SWG\BBDD\ds

rem set TUDELA=C:\Proyectos\Tudela\Tudela430
rem set TUDELA_DB_DIR=C:\Proyectos\Tudela\ds
set TUDELA=C:\Projects\Tudela\Tudela430
set SW_SAVE_IMAGE_DIR=%TUDELA%\images
set TUDELA_DB_DIR=C:\Projects\Tudela\ds

REM set SW_MESSAGE_DB_DIR=\\iberlanoff.dynu.net\Smallworld430\product\data

set SOMS_DIR = %TUDELA%\..\soms430
set FME_DIR = %TUDELA%\..\fme430
set SW_DXF_DIR = %TUDELA%\..\dxf430
set LOCALISATION_DIR = %TUDELA%\..\localization\product_lp
set SW_ADDON_GOOGLE_MAPS_DIR = %TUDELA%\..\sw_addon_google_maps111