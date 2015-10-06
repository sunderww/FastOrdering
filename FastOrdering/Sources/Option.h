//
//  Option.h
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Option : NSManagedObject

@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSManagedObject *category;
@property (nonatomic, retain) NSSet *ordered;
@end

@interface Option (CoreDataGeneratedAccessors)

- (void)addOrderedObject:(NSManagedObject *)value;
- (void)removeOrderedObject:(NSManagedObject *)value;
- (void)addOrdered:(NSSet *)values;
- (void)removeOrdered:(NSSet *)values;

@end
