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
#import "OrderContent+Custom.h"
#import "OrderedDish.h"
#import "MenuModel.h"
#import "AppDelegate.h"
#import "NSManagedObject+create.h"

@implementation Order (Custom)

NSObject *  dictSafeValue(NSObject *obj) {
  return obj ? obj : @"";
}

- (NSDictionary *)toJSON {
  NSMutableDictionary * dict = [NSMutableDictionary new];
  
  dict[@"numTable"] = dictSafeValue(self.numTable);
  dict[@"numPA"] = dictSafeValue(self.dinerNumber);
  dict[@"globalComment"] = dictSafeValue(self.comments);
  dict[@"order"] = [NSMutableArray new];
  
  for (OrderContent * orderContent in self.orderContents) {
    NSMutableDictionary * menuDict = [NSMutableDictionary new];
    
    menuDict[@"menuId"] = dictSafeValue(orderContent.menuComposition.menu.serverId);
    menuDict[@"content"] = [NSMutableArray new];

    for (OrderedDish * dish in orderContent.dishes) {
      NSMutableDictionary * dishDict = [NSMutableDictionary new];
      
      dishDict[@"id"] = dictSafeValue(dish.dish.serverId);
      dishDict[@"qty"] = dictSafeValue(dish.quantity);
      dishDict[@"comment"] = dictSafeValue(dish.comment);
      dishDict[@"options"] = @"";
      [menuDict[@"content"] addObject:dishDict];
    }
    [dict[@"order"] addObject:menuDict];
  }
  
  for (OrderedDish * dish in self.dishes) {
    NSMutableDictionary * dishDict = [NSMutableDictionary new];
    
    dishDict[@"id"] = dictSafeValue(dish.dish.serverId);
    dishDict[@"qty"] = dictSafeValue(dish.quantity);
    dishDict[@"comment"] = dictSafeValue(dish.comment);
    dishDict[@"options"] = @"";
    [dict[@"order"] addObject:dishDict];
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
	for (OrderContent * content in [NSSet setWithSet:self.orderContents]) {
		[content sanitizeInContext:context];
		if (content.isEmpty) {
			content.order = nil;
			[context deleteObject:content];
		}
	}
}

- (void)sanitize {
	return [self sanitizeInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

- (NSArray *)alacarteContents {
	NSMutableArray * contents = [NSMutableArray new];

	for (OrderContent * content in self.orderContents) {
		if ([content.menuComposition.menu.name isEqualToString:kMenuALaCarteName]) {
			[contents addObject:content];
		}
	}
	
	return contents;
}

- (NSArray *)createALaCarteContentsInContext:(NSManagedObjectContext *)context {
// Has to use existing orderContents first than create it if it doesn't exist
	NSMutableArray * contents = self.alacarteContents.mutableCopy;
	Menu * menu = [[MenuModel new] alacarte];
	
	for (MenuComposition * composition in menu.compositions) {
		BOOL found = NO;
		for (OrderContent * content in contents) {
			if ([content.menuComposition.serverId isEqualToString:composition.serverId]) {
				found = YES;
				break;
			}
		}
		
		if (!found) {
			OrderContent * content = [OrderContent createInContext:context];
			content.menuComposition = composition;
			content.order = self;
			[contents addObject:content];
		}
	}
	
	return contents;
}

- (NSArray *)createALaCarteContents {
	return [self createALaCarteContentsInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

- (OrderedDish *)orderedDishWithDish:(Dish *)dish andComposition:(MenuComposition *)composition {
	for (OrderContent * content in self.orderContents) {
		if ([content.menuComposition.serverId isEqualToString:composition.serverId]) {
			for (OrderedDish * ordered in content.dishes) {
				if ([ordered.dish.serverId isEqualToString:dish.serverId])
					return ordered;
			}
		}
	}
	
	return nil;
}

@end
