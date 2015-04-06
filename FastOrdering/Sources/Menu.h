//
//  Menu.h
//  FastOrdering
//
//  Created by Sunder on 06/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class MenuComposition;

@interface Menu : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *compositions;
@end

@interface Menu (CoreDataGeneratedAccessors)

- (void)addCompositionsObject:(MenuComposition *)value;
- (void)removeCompositionsObject:(MenuComposition *)value;
- (void)addCompositions:(NSSet *)values;
- (void)removeCompositions:(NSSet *)values;

@end
