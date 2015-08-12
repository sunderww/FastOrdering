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

@interface OrderALaCarteModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, UITextFieldDelegate, UIScrollViewDelegate> {
	NSArray * compositions;
	NSArray * dishes;
	NSArray * orderContents;

	UITextField * responder;
}

- (instancetype)initWithOrder:(Order *)order;
- (void)reloadData;

@property (nonatomic, assign) Order *	order;
@property (nonatomic, assign) BOOL		editing;

@end
