//
//  Menu.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class MenuComposition, OrderedDish;

@interface Menu : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *compositions;
@property (nonatomic, retain) NSSet *orderedDishes;
@end

@interface Menu (CoreDataGeneratedAccessors)

- (void)addCompositionsObject:(MenuComposition *)value;
- (void)removeCompositionsObject:(MenuComposition *)value;
- (void)addCompositions:(NSSet *)values;
- (void)removeCompositions:(NSSet *)values;

- (void)addOrderedDishesObject:(OrderedDish *)value;
- (void)removeOrderedDishesObject:(OrderedDish *)value;
- (void)addOrderedDishes:(NSSet *)values;
- (void)removeOrderedDishes:(NSSet *)values;

@end
