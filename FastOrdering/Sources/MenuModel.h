//
//  MenuModel.h
//  FastOrdering
//
//  Created by Sunder on 28/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Menu.h"

#define kMenuALaCarteName		@"alacarte"


@interface MenuModel : NSObject

// Set a special context or AppDelegate's if it is nil
@property (nonatomic, retain) NSManagedObjectContext *	context;

// Return all the menus without the alacarte menu
- (NSArray *)menus;

// Return only the alacarte menu
- (Menu *)alacarte;

@end
