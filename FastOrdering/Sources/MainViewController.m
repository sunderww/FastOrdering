//
//  MainViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MainViewController.h"
#import "NotificationViewController.h"
#import "TablePlanViewController.h"
#import "MenuViewController.h"
#import "AboutViewController.h"
#import "AppDelegate.h"
#import "SocketHelper.h"
#import "Order.h"
#import "Notification.h"
#import "NSManagedObject+create.h"
#import "OrderCell.h"
#import "NotificationCell.h"
#import "CommandViewController.h"
#import "HistoryViewController.h"

// Uncomment the following line to skip the sync
//#define SKIP_SYNC

#define kTableViewCellHeight    55

@interface MainViewController ()

@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	
	((AppDelegate *)UIApplication.sharedApplication.delegate).mainController = self;

    [self syncDatabase];
    panelShown = NO;
    panelView.hidden = NO;
    overlay.alpha = 0;

    titleLabel.text = NSLocalizedString(@"Main Page", @"");
    lastNotificationsLabel.text = NSLocalizedString(@"Last Notifications", @"");
    lastOrdersLabel.text = NSLocalizedString(@"Last Orders", @"");
    noOrderLabel.text = NSLocalizedString(@"No order", @"");
    noNotificationLabel.text = NSLocalizedString(@"No notification", @"");
    for (UIButton * button in panelButtons)
        [button setTitle:NSLocalizedString(button.titleLabel.text, @"").capitalizedString forState:UIControlStateNormal];
    [orderButton setTitle:NSLocalizedString(@"order", @"").uppercaseString forState:UIControlStateNormal];
	[[SocketHelper sharedHelper] registerListener:self forEvent:@"receive_order"];
}

- (void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];

	[self reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)reloadData {
	lastOrders = [Order last:2 withDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"createdAt" ascending:NO]]];
	lastNotifications = [Notification last:2 withDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO]]];
	
	CGRect frame = lastOrdersTableView.frame;
	frame.size.height = lastOrders.count * kTableViewCellHeight;
	lastOrdersTableView.frame = frame;
	frame = lastNotificationsTableView.frame;
	frame.size.height = lastNotifications.count * kTableViewCellHeight;
	lastNotificationsTableView.frame = frame;
	
	noOrderLabel.hidden = lastOrders.count > 0;
	noNotificationLabel.hidden = lastNotifications.count > 0;
	
	[lastOrdersTableView reloadData];
	[lastNotificationsTableView reloadData];
}

- (void)goToOrder:(Order *)order {
	// Basically code in buttonClicked: method to change the current view
	[self prepareChange];
	
	controller = [[CommandViewController alloc] initWithNibName:@"CommandView" bundle:nil];
	((CommandViewController *)controller).order = order;
	((CommandViewController *)controller).mainController = self;
//	((CommandViewController *)controller).navigationController = self.navigationController;
	titleLabel.text = NSLocalizedString(@"order", @"").capitalizedString;
	
	[self postChange];
}

- (void)goBackToMainPage {
	[self prepareChange];
	
	controller = nil;
	mainView.hidden = NO;
	titleLabel.text = NSLocalizedString(@"Main Page", @"");
	[self viewDidAppear:NO];
	
	[self postChange];
}

- (BOOL)onMainPage {
	return !mainView.hidden;
}

#pragma mark - Helper methods

/*
 * This function should be called with a timer every X seconds.
 * It checks that the sync did work
 */
- (void)checkLoadStatus {
	if (!hasLoaded) {
		loaderView.hidden = YES;
		syncButton.enabled = YES;
		[timer invalidate];
		[[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"").capitalizedString message:NSLocalizedString(@"Sync_ErrorMessage", @"") delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
	}
	
	hasLoaded = NO;
}

- (void)syncDatabase {
    SyncHelper * syncer = [SyncHelper new];
	
	if (syncing) return ;
	syncing = YES;

	syncer.restaurantId = ((AppDelegate *)UIApplication.sharedApplication.delegate).restaurantId;
    classesToSync = 0;
	hasLoaded = NO;
	loaderView.hidden = NO;
	syncButton.enabled = NO;
	timer = [NSTimer scheduledTimerWithTimeInterval:8 target:self selector:@selector(checkLoadStatus) userInfo:nil repeats:YES];

#ifdef SKIP_SYNC
	NSArray * classes = @[];
#else
	NSArray * classes = @[@"Option", @"OptionCategory", @"DishCategory", @"Dish", @"Order", @"OrderedDish", @"OrderedOption", @"Menu", @"MenuComposition"];
#endif
    syncer.delegate = self;
    for (NSString * class in classes) {
        classesToSync++;
        [syncer syncClassNamed:class];
    }
    [syncer syncDeletedObjectsOfClasses:classes];
#ifdef SKIP_SYNC
	[self syncEnded];
#endif
}

- (void)syncEnded {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSError * error;

	syncing = NO;
	[timer invalidate];
	if ([context save:&error]) {
		DLog(@"SYNC EXECUTED, CONTEXT SAVED");
	} else {
		PPLog(@"%@", error);
	}
    DLog(@"END SYNC");
	[self viewDidAppear:YES];
	loaderView.hidden = YES;
	syncButton.enabled = YES;
}

- (void)hidePanel {
    if (panelShown)
        [self showPanel];
}

- (void)prepareChange {
	[controller.view removeFromSuperview];
	mainView.hidden = YES;
}

- (void)postChange {
	[self hidePanel];
	CGRect frame = centralView.frame;
	frame.origin = CGPointZero;
	controller.view.frame = frame;
	[centralView addSubview:controller.view];
}

#pragma mark - IBAction methods

- (IBAction)reSync {
	if (!self.doNotSync) {
		[self syncDatabase];
		[self reloadData];
	}
}

- (IBAction)showPanel {
    panelShown = !panelShown;
    
    [UIView animateWithDuration:0.4 animations:^{
        CGRect frame = panelView.frame;
        frame.origin.x += frame.size.width * (panelShown ? 1 : -1);
        panelView.frame = frame;
        overlay.alpha = panelShown ? 1 : 0;
    }];
}

- (IBAction)buttonClicked:(id)sender {
	[self prepareChange];

    if (sender == takeOrderButton || sender == orderButton) {
        controller = [[CommandViewController alloc] initWithNibName:@"CommandView" bundle:nil];
		((CommandViewController *)controller).mainController = self;
//		((CommandViewController *)controller).navigationController = self.navigationController;
        titleLabel.text = NSLocalizedString(@"order", @"").capitalizedString;
    } else if (sender == historyButton) {
        controller = [[HistoryViewController alloc] initWithNibName:@"HistoryView" bundle:nil];
		((HistoryViewController *)controller).mainController = self;
        titleLabel.text = NSLocalizedString(@"history", @"").capitalizedString;
    } else if (sender == notificationsButton) {
        controller = [[NotificationViewController alloc] initWithNibName:@"NotificationView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"notifications", @"").capitalizedString;
    } else if (sender == aboutButton) {
        controller = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"about", @"").capitalizedString;
    } else { // MainButton or unknown button
        controller = nil;
        mainView.hidden = NO;
        titleLabel.text = NSLocalizedString(@"Main Page", @"");
		[self viewDidAppear:NO];
    }

	[self postChange];
}

- (IBAction)logOut {
	[SocketHelper disconnect];
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - ParseSyncer delegate methods

- (void)syncDidFailWithError:(NSError *)error forClass:(NSString *)className {
    PPLog(@"%@ > %@", className, error);
    if (!--classesToSync)
        [self syncEnded];
}

- (void)syncDidStopForClass:(NSString *)className {
	DLog(@"sync OK %@", className);
	[timer invalidate];
	if (!--classesToSync) {
        [self syncEnded];
	} else {
		[timer fire];
	}
	hasLoaded = YES;
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return ((tableView == lastOrdersTableView) ? lastOrders.count : lastNotifications.count);
}

- (UITableViewCell *)orderCellAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"OrderCell";

    OrderCell * cell = [lastOrdersTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:@"OrderCell" owner:self options:nil][0];
    }

    [cell setOrder:lastOrders[indexPath.row]];
    return cell;
}

- (UITableViewCell *)notificationCellAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"notificationCell";
    
    NotificationCell * cell = [lastNotificationsTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
    }
    
    [cell setNotification:lastNotifications[indexPath.row]];
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return ((tableView == lastOrdersTableView) ? [self orderCellAtIndexPath:indexPath] : [self notificationCellAtIndexPath:indexPath]);
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (tableView != lastOrdersTableView) return;
	
	Order * order = lastOrders[indexPath.row];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];

	[self goToOrder:order];
}

#pragma mark - SocketIO Delegate methods

- (void)socketReceivedEvent:(NSString *)name withPacket:(SocketIOPacket *)packet {
	if (![name isEqualToString:@"receive_order"]) return ;

	[self reSync];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
