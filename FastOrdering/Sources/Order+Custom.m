//
//  Order+Custom.m
//  FastOrdering
//
//  Created by Sunder on 06/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "Order+Custom.h"
#import "Table.h"
#import "MenuComposition.h"
#import "Dish.h"
#import "Menu.h"
#import "OrderedDish+Custom.h"
#import "MenuModel.h"
#import "AppDelegate.h"
#import "NSManagedObject+create.h"
#import "OrderedOption.h"
#import "Option.h"

@implementation Order (Custom)

NSObject *  dictSafeValue(NSObject *obj) {
	return obj ? obj : @"";
}

- (NSDictionary *)toJSONTest {
	NSMutableDictionary * dict = [NSMutableDictionary new];
//
//	dict[@"numTable"] = dictSafeValue(self.numTable);
//	dict[@"numPA"] = dictSafeValue(self.dinerNumber);
//	dict[@"globalComment"] = dictSafeValue(self.comments);
//	NSMutableArray * order = [NSMutableArray new];
//	
//	for (OrderContent * orderContent in self.orderContents) {
//		NSMutableDictionary * menuDict = [NSMutableDictionary new];
//		
//		menuDict[@"menuId"] = dictSafeValue(orderContent.menuComposition.menu.serverId);
//		menuDict[@"content"] = [NSMutableOrderedSet new];
//		NSMutableArray * content = [NSMutableArray new];
//		
//		for (OrderedDish * dish in orderContent.dishes) {
//			NSMutableDictionary * dishDict = [NSMutableDictionary new];
//			
//			dishDict[@"id"] = dictSafeValue(dish.dish.serverId);
//			dishDict[@"qty"] = dictSafeValue(dish.quantity);
//			dishDict[@"comment"] = dictSafeValue(dish.comment);
////			dishDict[@"options"] = @"";
//			[content addObject:dishDict];
//		}
//		menuDict[@"content"] = [NSCountedSet setWithArray:content];
//		[order addObject:menuDict];
//	}
//	
//	for (OrderedDish * dish in self.dishes) {
//		NSMutableDictionary * dishDict = [NSMutableDictionary new];
//		
//		dishDict[@"id"] = dictSafeValue(dish.dish.serverId);
//		dishDict[@"qty"] = dictSafeValue(dish.quantity);
//		dishDict[@"comment"] = dictSafeValue(dish.comment);
////		dishDict[@"options"] = @"";
//		[order addObject:dishDict];
//	}
//
//	dict[@"order"] = [NSCountedSet setWithArray:order];
	return dict;
}

- (NSDictionary *)toJSON {
	NSMutableDictionary * dict = [NSMutableDictionary new];
	
	dict[@"numTable"] = dictSafeValue(self.numTable);
	dict[@"numPA"] = dictSafeValue(self.dinerNumber);
	dict[@"globalComment"] = dictSafeValue(self.comments);
	dict[@"order"] = [NSMutableArray new];
	
	for (NSArray * dishes in self.dishesGroupedByMenu) {
		NSMutableDictionary * menuDict = [NSMutableDictionary new];
		
		menuDict[@"menuId"] = dictSafeValue(((OrderedDish *)dishes.firstObject).menu.serverId);
		menuDict[@"content"] = [NSMutableArray new];
		
		for (OrderedDish * dish in dishes) {
			NSMutableDictionary * dishDict = [NSMutableDictionary new];
			
			dishDict[@"id"] = dictSafeValue(dish.dish.serverId);
			dishDict[@"qty"] = dictSafeValue(dish.quantity);
			dishDict[@"comment"] = dictSafeValue(dish.comment);
			dishDict[@"options"] = [NSMutableArray new];
			for (OrderedOption * option in dish.options.allObjects)
				[dishDict[@"options"] addObject:@{@"id": option.option.serverId, @"qty": option.qty}];

			[menuDict[@"content"] addObject:dishDict];
		}
		[dict[@"order"] addObject:menuDict];
	}
	
	return dict;
}

- (NSData *)toJSONData {
	return [NSJSONSerialization dataWithJSONObject:self.toJSON options:0 error:NULL];
}

- (NSString *)toJSONString {
	NSData * data = self.toJSONData;
	return data ? [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding] : nil;
}

- (void)sanitizeInContext:(NSManagedObjectContext *)context {
	for (OrderedDish * dish in [NSSet setWithSet:self.dishes]) {
		[dish sanitizeInContext:context];
		if (dish.quantity.integerValue == 0) {
			dish.order = nil;
			dish.menu = nil;
			dish.dish = nil;
			[context deleteObject:dish];
		}
	}
}

- (void)sanitize {
	return [self sanitizeInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

- (NSArray *)dishesGroupedByMenu {
	NSMutableArray * contents = [NSMutableArray new];
	
//	for (OrderContent * content in self.orderContents) {
//		if ([content.menuComposition.menu.name isEqualToString:kMenuALaCarteName]) {
//			[contents addObject:content];
//		}
//	}
	
	return contents;
}

- (NSArray *)alacarteDishesInContext:(NSManagedObjectContext *)context {
	NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:@"OrderedDish"];
	NSArray * results;
	NSError * error;
	
	request.predicate = [NSPredicate predicateWithFormat:@"menu.name = %@ AND order.serverId = %@", kMenuALaCarteName, self.serverId];
	results = [context executeFetchRequest:request error:&error];
	if (error)
		PPLog(@"%@", error);
	return results;
}

- (NSArray *)alacarteDishes {
	return [self alacarteDishesInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

- (OrderedDish *)orderedDishWithDish:(Dish *)dish andComposition:(MenuComposition *)composition {
	for (OrderedDish * ordered in self.dishes) {
		if ([ordered.dish.serverId isEqualToString:dish.serverId] &&
			[ordered.menu.serverId isEqualToString:composition.menu.serverId])
			return ordered;
	}
	
	return nil;
}

@end
