//
//  OrderMenuModel.h
//  FastOrdering
//
//  Created by Sunder on 20/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SLExpandableTableView.h"

@interface OrderMenuModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate> {
  NSArray * menus;
  NSArray * compositions;
}

@end
