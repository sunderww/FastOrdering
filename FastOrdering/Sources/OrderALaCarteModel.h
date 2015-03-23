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

@protocol OrderALaCarteDelegate <NSObject>

- (void)dishClicked:(Dish *)dish;

@end

@interface OrderALaCarteModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate> {

}

@property (nonatomic, retain) id<OrderALaCarteDelegate> delegate;

@end
