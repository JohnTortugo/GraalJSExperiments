package com.jtortugo.proxies;

import java.util.ArrayList;
import java.util.List;

public class ProxyFieldAccess {
	private static final ArrayList<ExperimentConfig> experimetConfigs = new ArrayList<>(List.of(
//		new ExperimentConfig("ForLoop_Multiply_Add_Single_Read_Field", 
//			"""
//			(
//        		function for_multiply_add(order, length) {
//        		    let result = 0;
//        		    for (var i = 0; i < length; i++) {
//						result += 3.1415 * order.field1;
//        			}
//        			return result;
//				}
//            )
//            """)
//			.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_OneField(i);        		})
//			.addProxyConfig("DirectAccess_SingleField_ProxyObject", 	i -> { return new DirectAccess_OneField_ProxyObject(i); })
//			.addProxyConfig("MapAccess_SingleField_ProxyObject", 		i -> { return new MapAccess_OneField_ProxyObject(i); 	})
//			.addProxyConfig("MapAccess_SingleField_CustomProxy", 		i -> { return new MapAccess_OneField_CustomProxy(i); 	}),
//					
//					
//		new ExperimentConfig("ForLoop_Multiply_Add_Two_Read_Same_Field", 
//			"""
//			(
//        		function for_multiply_add_same_field(order, length) {
//        		    let result = 0;
//        		    for (var i = 0; i < length; i++) {
//						result += order.field1 * order.field1;
//        			}
//        			return result;
//				}
//            )
//            """)
//			.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_OneField(i);        		})
//			.addProxyConfig("DirectAccess_SingleField_ProxyObject", 	i -> { return new DirectAccess_OneField_ProxyObject(i); })
//			.addProxyConfig("MapAccess_SingleField_ProxyObject", 		i -> { return new MapAccess_OneField_ProxyObject(i); 	})
//			.addProxyConfig("MapAccess_SingleField_CustomProxy", 		i -> { return new MapAccess_OneField_CustomProxy(i); 	}),
//			
//		
//		/************************************************************************************************************************/
//		new ExperimentConfig("ForLoop_Multiply_Add_Four_Read_Fields", 
//			"""
//			(
//        		function for_multiply_four_fields(order, length) {
//        		    let result = 0;
//        		    for (var i = 0; i < length; i++) {
//						result += order.field1 * order.field2 * order.field3 * order.field4;
//        			}
//        			return result;
//				}
//            )
//            """)
//		    .addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_FourFields(i, i+1, i+2, i+3);       			})
//			.addProxyConfig("DirectAccess_FourFields_ProxyObject", 		i -> { return new DirectAccess_FourFields_ProxyObject(i, i+1, i+2, i+3);	})
//			.addProxyConfig("MapAccess_FourFields_ProxyObject", 		i -> { return new MapAccess_FourFields_ProxyObject(i, i+1, i+2, i+3); 		})
//			.addProxyConfig("MapAccess_FourFields_CustomProxy", 		i -> { return new MapAccess_FourFields_CustomProxy(i, i+1, i+2, i+3); 		}),
//			
//			
//		/************************************************************************************************************************/
//		new ExperimentConfig("2Level_ForNest_Multiply_Add_Four_Read_Fields", 
//			"""
//			(
//        		function for_multiply_four_fields(order, length) {
//        		    let result = 0;
//        		    for (var i = 0; i < length/1000; i++) {
//		     		    for (var j = 0; j < length/100; j++) {
//							result += order.field1 * order.field2 * order.field3 * order.field4;
//						}
//        			}
//        			return result;
//				}
//            )
//            """)
//			.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_FourFields(i, i+1, i+2, i+3);       			})
//			.addProxyConfig("DirectAccess_FourFields_ProxyObject", 		i -> { return new DirectAccess_FourFields_ProxyObject(i, i+1, i+2, i+3);    })
//			.addProxyConfig("MapAccess_FourFields_ProxyObject", 		i -> { return new MapAccess_FourFields_ProxyObject(i, i+1, i+2, i+3); 		})
//			.addProxyConfig("MapAccess_FourFields_CustomProxy", 		i -> { return new MapAccess_FourFields_CustomProxy(i, i+1, i+2, i+3); 		}),
//			
//			
//			/************************************************************************************************************************/
//			new ExperimentConfig("1_2_Level_ForNest_Multiply_Add_Four_RW_Fields", 
//				"""
//				(
//	        		function for_multiply_four_fields(order, length) {
//	        		    let result = 0;
//	        		    for (var i = 0; i < length/1000; i++) {
//			     		    for (var j = 0; j < length/1000; j++) {
//								result += order.field1 * order.field2 * order.field3 * order.field4;
//								order.field1 = order.field4++;
//								order.field2 = order.field3++;
//								order.field3 = order.field2++; 
//								order.field4 = order.field1++;
//							}
//							
//			     		    for (var j = 0; j < length/1000; j++) {
//								result += order.field1 * order.field2 * order.field3 * order.field4;
//								order.field1 = order.field4++;
//								order.field2 = order.field3++;
//								order.field3 = order.field2++; 
//								order.field4 = order.field1++;
//							}
//	        			}
//	        			return result;
//					}
//	            )
//	            """)
//				.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_FourFields(i, i+1, i+2, i+3);       			})
//				.addProxyConfig("DirectAccess_FourFields_ProxyObject", 		i -> { return new DirectAccess_FourFields_ProxyObject(i, i+1, i+2, i+3);    })
//				.addProxyConfig("MapAccess_FourFields_ProxyObject", 		i -> { return new MapAccess_FourFields_ProxyObject(i, i+1, i+2, i+3); 		})
//				.addProxyConfig("MapAccess_FourFields_CustomProxy", 		i -> { return new MapAccess_FourFields_CustomProxy(i, i+1, i+2, i+3); 		}),
//				
//				
//				
//				/************************************************************************************************************************/
//				new ExperimentConfig("find_in_two_lists", 
//					"""
//					(
//		        		function find_in_two_lists(left, right) {
//		        		    let result = 0;
//		        		    for (var i = 0; i < left.length; i++) {
//				     		    for (var j = 0; j < right.length; j++) {
//									if (left[i].id === right[j].id) {
//										return true;
//									}
//								}
//							}
//							return false;
//						}
//		            )
//		            """)
//					.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_FourFields(i, i+1, i+2, i+3);       			})
//					.addProxyConfig("DirectAccess_FourFields_ProxyObject", 		i -> { return new DirectAccess_FourFields_ProxyObject(i, i+1, i+2, i+3);    })
//					.addProxyConfig("MapAccess_FourFields_ProxyObject", 		i -> { return new MapAccess_FourFields_ProxyObject(i, i+1, i+2, i+3); 		})
//					.addProxyConfig("MapAccess_FourFields_CustomProxy", 		i -> { return new MapAccess_FourFields_CustomProxy(i, i+1, i+2, i+3); 		}),
//					
//
//
//				/************************************************************************************************************************/
//				new ExperimentConfig("read_after_read",
//					"""
//                    (
//                        function read_after_read(input) {
//                            return input.field1 + input.field1 + input.field1;
//                        }
//                    )
//                    """)
//					.addProxyConfig("DirectAccess", 							i -> { return new DirectAccess_OneField(i);        		})
//					.addProxyConfig("DirectAccess_SingleField_ProxyObject", 	i -> { return new DirectAccess_OneField_ProxyObject(i); })
//					.addProxyConfig("MapAccess_SingleField_ProxyObject", 		i -> { return new MapAccess_OneField_ProxyObject(i); 	})
//					.addProxyConfig("MapAccess_SingleField_CustomProxy", 		i -> { return new MapAccess_OneField_CustomProxy(i); 	})



				new ExperimentConfig("return_unchanged_input", """
                    (
                        function read_after_read(input) {
                            return input;
                        }
                    )
                    """)
			.addProxyConfig("MapAccess_SingleField_CustomProxy", MapAccess_OneField_CustomProxy::new)
	));
	
	  
    public static void main(String[] args) throws Exception {
    	for (var expConfig : experimetConfigs) {
    		System.out.println("Source: " + expConfig.name);
    		for (var proxyConfig : expConfig.proxyConfigs) {    			
    			BenchIt.benchIt(expConfig.name, expConfig.source, proxyConfig.name, proxyConfig.gen);
    		}
    	}
    }
}


/*
	function doSomething(order) {
		return order;
	}

	ProtonBooleanProxy order = new ProtonBooleanProxy();

	Value order2 = jsBindings.execute(order);

	ProtonBooleanProxy order3 = <...> order2;
	order3.getTerm();


 */





