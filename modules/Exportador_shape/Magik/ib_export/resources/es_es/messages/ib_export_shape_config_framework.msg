#% text_encoding = iso8859_1

:title		            Configuracion Avanzada: Mapeos Exportador SHAPE
:corresp_objs_entidades     Correspondencias: Objetos SW - Entidades SHAPE
:corresp_fields_attributes  Correspondencias: Campos SW - Atributos SHAPE

:c_object_sw                  Objeto SW
:c_geometry                   Campo Geometrico
:c_entity                     Entidad
:toggle_export_empty          V.

:bt_remove_selected           Eliminar Seleccionadas
:bt_reload                    Recargar Valores Iniciales

:bt_add_entity                Añadir Entidad
:bt_update_entity             Actualizar Entidad

:c_field_sw                   Campo SW
:c_field_type_sw              Tipo SW
:c_unit_sw                    Unidad SW
:c_attribute_sh               Atributo
:c_unit_sh                    Unidad SHAPE
:c_format_method              Metodo Formateo
:c_format_parameters          Parametros(;)

:not_geometries_fields        Campos No Geometricos

:bt_add_attribute             Añadir Atributo
:bt_update_attribute          Actualizar Atributo

:accept                     Aceptar
:cancel                     Cancelar
:exit                       Salir

:min_combo_width           120    
:min_column_width          100

:err_update_e_no_sel       No hay una entidad seleccionada para actualizar.
:err_update_e_obj          La correspondencia que se quiere actualizar no coincide con el Objeto SW seleccionado.
:err_update_e_geom         La correspondencia que se quiere actualizar no coincide con el Campo Geometrico seleccionado.

:err_add_e_exist           Ya existe la correspondencia de Objeto SW - Campo Geometrico - Entidad
:question_add_continue     Existe una correspondencia "Objeto SW - Campo Geometrico"
asociada a otro nombre de entidad. ¿Desea actualizar el valor de la entidad?

:yes                       SI
:no                        NO

:question_reload           Al recargar los mapeos iniciales se perderan los 
ultimos cambios realizados. ¿Desea continuar? 

:err_update_at_no_sel       No hay un atributo seleccionado para actualizar.
:err_no_valid_geom          #1 No es un valor de geometria valido para el objeto actual
:err_no_valid_entity        Ignorar correspondencia: #1 - #2 - #3 ,datos no validos
:bt_clear_attributes           Limpiar Datos Atributo

:ignore_changes             Ignorar Cambios
:save_changes               Guardar en Fichero

:save                       Guardar
:save_changes_and_exit      Guardar y Salir
:continue                   Continuar
:cancel                     Cancelar

:changes_with_save         Los cambios realizados en la configuracion de exportacion se han guardado
en un fichero.

- Si ademas desea utilizarlos para la exportacion actual pulse :  #1.
- Si quiere descartarlos pulse                                                   :  #2.
- Para volver a la ventana de configuracion pulsar                  :  #3.

:changes_without_save       Se han realizado cambios en la configuracion de exportacion.

- Si desea utilizarlos para la exportacion actual pulse :  #1.
- Si ademas desea guardarlos en un fichero pulse      :  #2.
- Si quiere descartarlos pulse                                      :  #3.
- Para volver a la ventana de configuracion pulsar     :  #4.

:exit_without_save          Desea ignorar los cambios realizados 
y salir de la ventana de configuracion?

:err_remove_attributes      Error al intentar eliminar una correspondencia Sw - Attributo
:not_selected_field         No hay ningun campo seleccionado
