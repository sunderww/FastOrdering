//
//  SyncHelper.m
//  FastOrdering
//
//  Created by Sunder on 02/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "SyncHelper.h"
#import "AppDelegate.h"
#import "SocketHelper.h"

#pragma mark - Parsing defines
#define kParsingIdKey               @"id"
#define kParsingCreatedAtKey        @"createdAt"
#define kParsingUpdatedAtKey        @"updatedAt"
#define kParsingRelationToOneSuffix @"_id"
#define kParsingRelationManySuffix  @"_ids"

#pragma mark - SyncHelper

@implementation SyncHelper

- (void)syncClassNamed:(NSString *)name {
    if ([self.delegate respondsToSelector:@selector(syncDidStartForClass:)])
        [self.delegate syncDidStartForClass:name];

    SocketIO * socket = [SocketHelper sharedSocket];
    [socket get:[@"/" stringByAppendingString:name] withData:nil callback:^(id response) {
        for (NSDictionary * elem in (NSArray *)response) {
            NSManagedObject * obj = [self objectOfClass:name forObject:elem];

            [self syncManagedObject:obj ofClass:name withDictionnary:elem];
            DPPLog(@"GOT %@", obj);
        }

        if ([self.delegate respondsToSelector:@selector(syncDidStopForClass:)])
            [self.delegate syncDidStopForClass:name];
    }];
}

- (void)syncDeletedObjectsOfClasses:(NSArray *)classes {
    DLog(@"SHOULD START DELETE SYNC");
}

#pragma mark - Helper methods

- (void)syncManagedObject:(NSManagedObject *)obj ofClass:(NSString *)className withDictionnary:(NSDictionary *)elem {
    NSDictionary * attributes = obj.entity.attributesByName;
    NSDictionary * relationships = obj.entity.relationshipsByName;
    
    for (NSString * key in elem) {
        id value = elem[key];
        if ([key hasSuffix:kParsingRelationToOneSuffix]) {
            NSString * name = [key stringByReplacingOccurrencesOfString:@"_id" withString:@""];
            NSRelationshipDescription * rel = relationships[name];

            if (!rel) continue;

            NSManagedObject * relation = [self objectOfClass:rel.destinationEntity.managedObjectClassName withId:value];
            if (rel.isToMany)
                name = [NSString stringWithFormat:@"add%@Object:", [name capitalizedString]];
            else
                name = [NSString stringWithFormat:@"set%@:", [name capitalizedString]];
            SEL selector = NSSelectorFromString(name);
            ((void (*)(id, SEL, id))[obj methodForSelector:selector])(obj, selector, relation);
        } else if ([key hasSuffix:kParsingRelationManySuffix]) {
            NSString * name = [key stringByReplacingOccurrencesOfString:@"_ids" withString:@""];
            NSRelationshipDescription * rel = relationships[name];

            if (![value isKindOfClass:[NSArray class]]) {
                if ([value isEqualToString:@""]) continue;
                value = @[value];
            }
            if (!rel || !rel.isToMany) {
                PPLog(@"no to-many relationship named %@", name);
                continue;
            }

            for (NSString * serverId in value) {
                NSManagedObject * relation = [self objectOfClass:rel.destinationEntity.managedObjectClassName withId:serverId];
                name = [NSString stringWithFormat:@"add%@Object:", [name capitalizedString]];
                SEL selector = NSSelectorFromString(name);
                ((void (*)(id, SEL, id))[obj methodForSelector:selector])(obj, selector, relation);
            }
        } else if (value && value != [NSNull null] &&
                   [attributes.allKeys containsObject:key]) {
            switch ([attributes[key] attributeType]) {
                case NSInteger16AttributeType:
                case NSInteger32AttributeType:
                case NSInteger64AttributeType:
                case NSDecimalAttributeType:
                case NSDoubleAttributeType:
                case NSFloatAttributeType:
                case NSBooleanAttributeType:
                    if ([[value class] isSubclassOfClass:[NSString class]]) {
                        NSNumberFormatter * f = [NSNumberFormatter new];
                        
                        f.numberStyle = NSNumberFormatterDecimalStyle;
                        value = [f numberFromString:value];
                    }
                    break;
                    
                case NSStringAttributeType:
                    if ([[value class] isSubclassOfClass:[NSNumber class]])
                        value = [value stringValue];
                    break;
                    
                case NSDateAttributeType:
                    if ([[value class] isSubclassOfClass:[NSString class]]) {
                        NSDateFormatter * f = [NSDateFormatter new];
                        
                        f.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSZ";
                        f.locale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
                        value = [f dateFromString:value];
                    }
                    break;
                    
                default:
                    break;
            }
            
            NSString * class = [attributes[key] attributeValueClassName];
            if ([[value class] isSubclassOfClass:NSClassFromString(class)])
                [obj setValue:value forKey:key];
            else
                PPLog(@"%@ != %@", class, [value class]);
        }
    }
}

- (NSManagedObject *)objectOfClass:(NSString *)name withId:(NSString *)serverId {
    return [self objectOfClass:name forObject:@{kParsingIdKey: serverId}];
}

- (NSManagedObject *)objectOfClass:(NSString *)name forObject:(NSDictionary *)obj {
    NSManagedObjectContext * context = self.managedObjectContext;
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:name];
    NSArray * results;
    NSError * error;

    NSString * identifier = [obj valueForKey:kParsingIdKey];
    request.predicate = [NSPredicate predicateWithFormat:@"serverId = %@" argumentArray:@[identifier]];
    results = [context executeFetchRequest:request error:&error];
    if (error)
        PPLog(@"%@", error);

    NSManagedObject * object;
    if (results.count)
        object = results.firstObject;
    if (!object) {
        DLog(@"Creating NSManagedObject of class %@", name);
        object = [NSEntityDescription insertNewObjectForEntityForName:name inManagedObjectContext:context];

        id serverId = [obj valueForKey:kParsingIdKey];
        if (![serverId isKindOfClass:[NSString class]])
            serverId = [serverId stringValue];
        [object setValue:serverId forKey:@"serverId"];
    }

    return object;
}

- (NSDate *)latestUpdateOfClass:(NSString *)class {
    NSSortDescriptor * sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO];
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:class];
    NSArray * results;
    NSDate * latestUpdate;
    NSError * error;
    
    [request setSortDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    [request setFetchLimit:1];
    
    results = [self.managedObjectContext executeFetchRequest:request error:&error];
    if (error) {
        if ([self.delegate respondsToSelector:@selector(syncDidFailWithError:forClass:)])
            [self.delegate syncDidFailWithError:error forClass:class];
        return nil;
    }
    
    if (results.count) {
        latestUpdate = [results.firstObject valueForKey:@"updatedAt"];
    } else
        latestUpdate = [NSDate dateWithTimeIntervalSince1970:0];
    return latestUpdate;
}

- (NSManagedObjectContext *)managedObjectContext {
    AppDelegate * appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
    return appDelegate.managedObjectContext;
}


@end
