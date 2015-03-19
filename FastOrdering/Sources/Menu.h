//
//  Menu.h
//  FastOrdering
//
//  Created by Sunder on 18/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Menu : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSSet *compositions;
@end

@interface Menu (CoreDataGeneratedAccessors)

- (void)addCompositionsObject:(NSManagedObject *)value;
- (void)removeCompositionsObject:(NSManagedObject *)value;
- (void)addCompositions:(NSSet *)values;
- (void)removeCompositions:(NSSet *)values;

@end
