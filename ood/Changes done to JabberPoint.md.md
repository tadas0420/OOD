# Changes done to JabberPoint

| Nr.  | Change                                                       |
| ---- | ------------------------------------------------------------ |
| 1.   | Deleted the AboutBox class and made a method in MenuController class because it looked like it was unnecessary |
|      | In Accesor class                                             |
| 2.   | Changed method variable names for consistency with XMLAccessor |
|      | In BitMapItem class                                          |
| 3.   | made imageName final                                         |
|      | In KeyController class                                       |
| 4.   | “p” renamed to “presentation” for consistency                |
| 5.   | Made presentation final                                      |
| 6.   | Reduced amounts of cases in the switch                       |
|      | In MenuController class                                      |
| 7.   | Made parent final because it’s assigned only once            |
|   8. | Refactored the Constructor because too many things were happening in a single constructor. So I extracted them           |
| 9.   | Public void actionPerfomed calls replaced with lambda        |
| 10.   | Removed redundant casting into “Object”                      |
| 11.  | Annotated serialVersionUID as @Serial                        |
| 12.  | Made the "Go to slide" function check if the number entered is valid |
|      | In SlideViewerFrame class                                    |
| 13.  | Annotated serialVersionUID as @Serial                        |
|      | In TextItem class                                            |
| 14.  | refactored getBoundingBox method and draw method                      |
| 15.  | Made "text" final                                            |
| 16. | Removed redundancies   |                                     
| 17.| Impelemented the Observer design pattern - When modified or changed in the presentation class the SlideViewerComponent is updated                                      |
|      | In Slide class   |   
|  18. | Refactored the draw method.   
|      | In XMLAccessor class   |   
|  19. | Deleted the DemoPresentation class and made a method loadDemoPresentation
 | 20. | Refactored LoadFile method and SaveFile

                                       
