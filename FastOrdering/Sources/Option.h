//
//  Option.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class OptionCategory, OrderedOption;

@interface Option : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *categories;
@property (nonatomic, retain) NSSet *ordered;
@end

@interface Option (CoreDataGeneratedAccessors)

- (void)addCategoriesObject:(OptionCategory *)value;
- (void)removeCategoriesObject:(OptionCategory *)value;
- (void)addCategories:(NSSet *)values;
- (void)removeCategories:(NSSet *)values;

- (void)addOrderedObject:(OrderedOption *)value;
- (void)removeOrderedObject:(OrderedOption *)value;
- (void)addOrdered:(NSSet *)values;
- (void)removeOrdered:(NSSet *)values;

@end
