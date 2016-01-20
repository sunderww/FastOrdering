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

// Uncomment the following line to use Socket.IO instead of HTTP Request
//#define USE_SOCKET_REQUEST

#pragma mark - Parsing defines
#define kParsingIdKey               @"id"
#define kParsingCreatedAtKey        @"createdAt"
#define kParsingUpdatedAtKey        @"updatedAt"
#define kParsingRelationToOneSuffix @"_id"
#define kParsingRelationManySuffix  @"_ids"
#define kParameterRestaurantId		@"restaurant"

#pragma mark - SyncHelper

@implementation SyncHelper

- (instancetype)init {
	if (self = [super init]) {
		self.stopped = NO;
	}
	
	return self;
}

- (void)syncClassNamed:(NSString *)name {
	if (self.stopped)
		return ;

	if ([self.delegate respondsToSelector:@selector(syncDidStartForClass:)])
		[self.delegate syncDidStartForClass:name];
	
#ifndef USE_SOCKET_REQUEST
	NSDateFormatter * formatter = [NSDateFormatter new];
	NSTimeZone * timeZone = [NSTimeZone timeZoneWithName:@"UTC"];
	formatter.timeZone = timeZone;
	formatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSS";

	NSString * lastUpdate = [formatter stringFromDate:[self latestUpdateOfClass:name]];
	NSString * params = [NSString stringWithFormat:@"?%@=%@&from=%@", kParameterRestaurantId, self.restaurantId, lastUpdate];
	NSURL * url = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@:%d/%@%@", kSocketIOHost, kSocketIOPort, name, params]];
	
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
		NSError * error;
		NSData * data = [NSData dataWithContentsOfURL:url options:kNilOptions error:&error];
		
		if (error) {
			PPLog(@"ERROR : %@", error);
			if ([self.delegate respondsToSelector:@selector(syncDidFailWithError:forClass:)])
				[self.delegate syncDidFailWithError:error forClass:name];
			return ;
		}

		NSArray * results = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
		dispatch_async(dispatch_get_main_queue(), ^{
			if (error) {
				PPLog(@"ERROR : %@", error);
				if ([self.delegate respondsToSelector:@selector(syncDidFailWithError:forClass:)])
					[self.delegate syncDidFailWithError:error forClass:name];
				return ;
			}
			
			for (NSDictionary * elem in results) {
				NSManagedObject * obj = [self objectOfClass:name forObject:elem];
				
				[self syncManagedObject:obj ofClass:name withDictionnary:elem];
				DPPLog(@"GOT %@", obj);
			}
		
			if ([self.delegate respondsToSelector:@selector(syncDidStopForClass:)])
				[self.delegate syncDidStopForClass:name];
		});
	});
	
#else
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
#endif
}

- (void)deleteEmptyObjectsOfClass:(NSString *)className inContext:(NSManagedObjectContext *)context {
	NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:className];
	NSArray * results;
	NSError * error;
	
	request.predicate = [NSPredicate predicateWithFormat:@"serverId = nil"];
	
	results = [context executeFetchRequest:request error:&error];
	if (error)
		PPLog(@"%@", error);

	for (NSManagedObject * obj in results)
		[context deleteObject:obj];
}

- (void)deleteEmptyObjectsOfClass:(NSString *)className {
	NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
	[self deleteEmptyObjectsOfClass:className inContext:context];
}

- (void)syncDeletedObjectsOfClasses:(NSArray *)classes {
	for (NSString * className in classes) {
		[self deleteEmptyObjectsOfClass:className];
	}
}

#pragma mark - Helper methods

- (id)correctValueForKey:(NSString *)key attributes:(NSDictionary *)attributes andValue:(id)value {
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
	
	return value;
}

- (void)syncManagedObject:(NSManagedObject *)obj ofClass:(NSString *)className withDictionnary:(NSDictionary *)elem {
	NSDictionary * attributes = obj.entity.attributesByName;
	NSDictionary * relationships = obj.entity.relationshipsByName;
	
	for (NSString * key in elem) {
		id value = elem[key];
		if (value == [NSNull null] || [key isEqualToString:kParsingIdKey]) continue ;
		
		if ([key hasSuffix:kParsingRelationToOneSuffix]) {
			NSString * name = [key stringByReplacingOccurrencesOfString:@"_id" withString:@""];
			NSRelationshipDescription * rel = relationships[name];
			
			if (rel) {
				NSManagedObject * relation = [self objectOfClass:rel.destinationEntity.managedObjectClassName withId:value];
				NSString *firstCapChar = [[name substringToIndex:1] capitalizedString];
				NSString *cappedString = [name stringByReplacingCharactersInRange:NSMakeRange(0,1) withString:firstCapChar];
				if (rel.isToMany) {
					name = [NSString stringWithFormat:@"add%@Object:", cappedString];
				} else
					name = [NSString stringWithFormat:@"set%@:", cappedString];
				SEL selector = NSSelectorFromString(name);
				((void (*)(id, SEL, id))[obj methodForSelector:selector])(obj, selector, relation);
				continue;
			} else {
				value = [self correctValueForKey:key attributes:attributes andValue:value];
			}
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
			
			name = [NSString stringWithFormat:@"add%@Object:", [name capitalizedString]];
			SEL selector = NSSelectorFromString(name);
			for (NSString * serverId in value) {
				NSManagedObject * relation = [self objectOfClass:rel.destinationEntity.managedObjectClassName withId:serverId];
				((void (*)(id, SEL, id))[obj methodForSelector:selector])(obj, selector, relation);
			}
			continue;
		} else if (value && value != [NSNull null] &&
				   [attributes.allKeys containsObject:key]) {
			value = [self correctValueForKey:key attributes:attributes andValue:value];
		}
		
		NSString * class = [attributes[key] attributeValueClassName];
		if ([[value class] isSubclassOfClass:NSClassFromString(class)])
			[obj setValue:value forKey:key];
		else
			PPLog(@"%@:%@ > %@ != %@ (%@)", className, key, class, [value class], value);
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
		if ([serverId isEqual:[NSNull null]]) {
			[context deleteObject:object];
			return NULL;
		}
		
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
