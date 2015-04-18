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
#import "OrderContent.h"
#import "OrderedDish.h"

@implementation Order (Custom)

NSObject *  dictSafeValue(NSObject *obj) {
  return obj ? obj : @"";
}

- (NSDictionary *)toJSON {
  NSMutableDictionary * dict = [NSMutableDictionary new];
  
  dict[@"numTable"] = dictSafeValue(self.table.name);
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

@end
