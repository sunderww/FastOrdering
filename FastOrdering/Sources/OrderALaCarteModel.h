//
//  OrderALaCarteModel.h
//  FastOrdering
//
//  Created by Sunder on 20/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SLExpandableTableView.h"
#import "Dish.h"
#import "Order.h"
#import "DishCell.h"
#import "Menu.h"

@interface OrderALaCarteModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, DishCellDelegate, UIScrollViewDelegate> {
	NSArray * 	compositions;
	NSArray *	dishes;
	Menu *		carteMenu;

	UITextField * responder;
}

- (instancetype)initWithOrder:(Order *)order;
- (void)reloadData;

@property (nonatomic, assign) Order *	order;
@property (nonatomic, assign) BOOL		editing;

@end
