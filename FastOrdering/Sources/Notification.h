//
//  Notification.h
//  FastOrdering
//
//  Created by Sunder on 02/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Notification : NSManagedObject

@property (nonatomic, retain) NSString * contentString;

@end
